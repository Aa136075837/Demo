package com.smart.otacomponent;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.smart.smartble.SmartManager;
import com.smart.smartble.event.Action;
import com.smart.smartble.event.SmartAction;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author ARZE
 * @version 创建时间：2017/6/1 10:07
 * @说明
 */
public class OTAComponent extends AbOTAComponent implements UpdateHelper.UpdateInterface {

    private static final String TAG = "OTAComponent";

    private UpdateHelper mUpdateHelper;
    private FileDataTemp mFileDataTemp = new FileDataTemp();

    private Runnable mTempRunnable;
    private Timer mTimer;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private boolean isComplete = true;
    private Style mStyle = Style.SLOW;

    private static String mPath = Environment.getExternalStorageDirectory() + "/outfile";
    private static OTAComponent mOtaComponent;
    private boolean isChangeInterval = false;
    private long mLastTime = 0;
    private boolean hasChangeInterval = false;

    public static OTAComponent getInstance(SmartManager smartManager) {
        if (null == mOtaComponent) {
            synchronized (OTAComponent.class) {
                if (null == mOtaComponent) {
                    mOtaComponent = new OTAComponent(smartManager);
                }
            }
        }
        return mOtaComponent;
    }

    private OTAComponent(SmartManager smartManager) {
        super(smartManager);
        mSmartManager = smartManager;
        mUpdateHelper = new UpdateHelper(mPath);
        mUpdateHelper.setUpdateListener(this);
    }

    @Override
    public void registerComponent() {
        mSmartManager.getIService().registerComponent(this);
    }

    @Override
    public void unRegisterComponent() {
        mSmartManager.getIService().unRegisterComponent(this);
        mHandler.removeCallbacks(mTempRunnable);
    }

    @Override
    public void start() {
        startPath(null);
    }

    public void startPath(String path) {
        if (!TextUtils.isEmpty(path)) {
            mPath = path;
        }
        Log.w("OTAComponent", "startPath ::");
        mUpdateHelper.setPath(mPath);
        dispatchDealFile();
        mUpdateHelper.dealFile();
        isComplete = false;
    }

    private void buildTimer(long time) {
        if (isComplete)
            return;
        cancelTimer();
        mTimer = new Timer();
        mTimer.schedule(new OTATask(this), time);
    }

    private void cancelTimer() {
        if (null != mTimer) {
            mTimer.cancel();
        }
    }

    @Override
    public void dealAction(SmartAction action) {
        byte[] bytes = action.getBytes();
        switch (action.getAction()) {
            case REQUEST_ACTION_REPLAY_CHANGE_BLE:
                if (!isChangeInterval)
                    return;
                int tip = bytes[8] & 0xff;
                int successful = bytes[9] & 0xff;
                if (0 == successful && (0 == tip || 2 == tip)) {
                    if (mStyle == Style.NORMAL)
                        mStyle = Style.QUICKY;
                    hasChangeInterval = true;
                    startOTAAfterMessage();
                    isChangeInterval = false;
                } else if (0 != successful && 0 == tip) {
                    changeBleInterval(2);
                } else if (0 != successful && 1 == tip) {
                    changeBleInterval(3);
                } else if (0 != successful && 2 == tip) {

                }
                break;
            case REQUEST_ACTION_ASK_MCU_FILE:
                int flow = bytes[4] & 0xff;
                int index = ((bytes[8] & 0xff) << 8) + (bytes[9] & 0xff);
                Log.w("OTAComponent", " before return  dealAction::" + flow + "    or   " + index);
                if (-1 == flow || -1 == index) {
                    return;
                }
                Log.w("OTAComponent", " before return dealAction::" + flow + "    or   " + index);
                if (mUpdateHelper.getmFileMessage().size() > index) {
                    OTAFileMessage message = mUpdateHelper.getmFileMessage().get(index);
                    mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_ASK_MCU_FILE,
                            flow, message.getmId(), message.getmMainVersion(), message.getmMinorVersion(),
                            message.getmTestVersion(), message.getmChildFiles(), message.getmDataCount(), index);
                }
                break;
            case REQUEST_ACTION_ASK_FILE_DATA:
                if (System.currentTimeMillis() - mLastTime < 3 * 1000) {
                    return;
                }
                mLastTime = System.currentTimeMillis();
                flow = bytes[4] & 0xff;
                int id = ((bytes[8] & 0xff) << 8) + (bytes[9] & 0xff);
                index = ((bytes[10] & 0xff) << 8) + (bytes[11] & 0xff);
                mFileDataTemp.flow = flow;
                mFileDataTemp.id = id;
                mFileDataTemp.index = index;
                if (-1 == flow || -1 == index || -1 == id) {
                    return;
                }
                int status = 0;
                OTAFileMessage message = null;
                message = mUpdateHelper.getOTAMessageById(id);
                mFileDataTemp.message = message;
                if (null == message) {
                    status = 1;
                }
                mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_ASK_MCU_FILE_STATUS, flow, id, status);
                mHandler.removeCallbacks(mTempRunnable);
                if (0 == status) {
                    Log.w("OTAComponent", " REQUEST_ACTION_ASK_FILE_DATA ::" + "镜像id:" + id + " or  包数：" + index + "   or  状态： " + status + " 镜像总包数：" +
                            mFileDataTemp.message.getmDataCount());
                    mUpdateHelper.initHadSend(message);
                    message.setHasSend(true);
                    dispatchProgress(100, (100 * mUpdateHelper.getHasSendCount(mFileDataTemp.message, mFileDataTemp.index) / mUpdateHelper.getFileCount()));
                    sendData(2000);
                }
                break;
            case REQUEST_ACTION_ASK_MCU_DATA_OK:
                Log.w("OTAComponent", " REQUEST_ACTION_ASK_MCU_DATA_OK::");
                int rec = ((bytes[0] & 0xff) << 8) + (bytes[1] & 0xff);
                if (mFileDataTemp.index != rec)
                    return;
                mFileDataTemp.index = mFileDataTemp.index + 16;
                Log.w("OTAComponent", "run----------->" + mFileDataTemp.index);
                if (mFileDataTemp.index >= mFileDataTemp.message.getmDataCount())
                    return;
                dispatchProgress(100, (100 * mUpdateHelper.getHasSendCount(mFileDataTemp.message, mFileDataTemp.index) / mUpdateHelper.getFileCount()));
                mHandler.removeCallbacks(mTempRunnable);
                sendData(getStyleTime());
                break;
            case REQUEST_ACTION_SET_OTA_END:
                flow = bytes[4] & 0xff;
                int complete = (bytes[8] & 0xff);
                mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_NORMAL_ACK, flow, 0);
                if (0 == complete) {
                    dispatchProgress(100, 100);
                    dispatchComplete(complete);
                    if (Style.NORMAL == mStyle || Style.QUICKY == mStyle)
                        delayToSendMcu();
                    isComplete = true;
                    cancelTimer();
                } else if (5 == complete) {
                    dispatchComplete(complete);
                    isComplete = true;
                    cancelTimer();
                    mHandler.removeCallbacks(mTempRunnable);
                }
                break;
        }
    }

    public void changeBleInterval(int value) {
        mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_CHANGE_BLE_INTERVAL, value);
    }

    private long getStyleTime() {
        switch (mStyle) {
            case SLOW:
                return 8 * 1000;
            case NORMAL:
                return 50;
            case QUICKY:
                return 400;
            default:
                return 8 * 1000;
        }
    }

    public void delayToSendMcu() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                OTAFileMessage message = mUpdateHelper.getMainFileMessage();
                if (null != message) {
                    mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_BEGIN_UPDATE_MCU,
                            message.getmMainVersion(), message.getmMinorVersion(), message.getmTestVersion());
                }
            }
        }, 2000);
    }

    private void sendData(final long time) {
        buildTimer(15 * 1000);
        if (null == mFileDataTemp.message)
            return;
        byte[][] bytes = new byte[16][16];
        for (int i = mFileDataTemp.index; i < mFileDataTemp.index + 16; i++) {
            bytes[i - mFileDataTemp.index] = mUpdateHelper.getDataByIndex(mFileDataTemp.message, i);
        }
        final byte[][] sendBytes = bytes;
        final int sendIndex = mFileDataTemp.index;
        mHandler.postDelayed(mTempRunnable = new Runnable() {
            @Override
            public void run() {
                mSmartManager.getIClient().doAction(com.smart.smartble.event.Action.REQUEST_ACTION_ASK_FILE_DATA, sendIndex, sendBytes);
            }
        }, time);
    }

    @Override
    public void dealFileSuccessful(OTAFileMessage message) {
        Log.w("OTAComponent", "dealFile uscc ::");
        isChangeInterval = true;
        changeBleInterval(0);
    }

    private class FileDataTemp {
        int id;
        int flow;
        int index;
        OTAFileMessage message;
    }

    private static class OTATask extends TimerTask {

        private WeakReference<OTAComponent> mTarget;

        public OTATask(OTAComponent component) {
            mTarget = new WeakReference<>(component);
        }

        @Override
        public void run() {
            if (null != mTarget.get()) {
                mTarget.get().mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        OTAFileMessage message = mTarget.get().mUpdateHelper.getMainFileMessage();
                        if (null != message) {
                            Log.w("OTAComponent", " 超时::重新请求");
                            mTarget.get().mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_START_OTA,
                                    message.getmMainVersion(), message.getmMinorVersion(), message.getmTestVersion());
                        }
                    }
                });
            }
        }
    }

    private void startOTAAfterMessage() {
        if (null == mUpdateHelper)
            return;
        OTAFileMessage message = mUpdateHelper.getMainFileMessage();
        if (null != message) {
            mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_START_OTA,
                    message.getmMainVersion(), message.getmMinorVersion(), message.getmTestVersion());
        }
        dispatchStart();
    }

    public enum Style {
        NORMAL,
        QUICKY,
        SLOW,
    }

    public void setStyle(Style mStyle) {
        this.mStyle = mStyle;
        if (hasChangeInterval) {
            if (Style.NORMAL == this.mStyle)
                this.mStyle = Style.QUICKY;
        }
    }
}
