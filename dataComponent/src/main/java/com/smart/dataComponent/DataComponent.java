package com.smart.dataComponent;

import android.util.Log;

import com.smart.dataComponent.data.ContentData;
import com.smart.dataComponent.data.ContentMessage;
import com.smart.smartble.ByteToString;
import com.smart.smartble.SmartManager;
import com.smart.smartble.event.Action;
import com.smart.smartble.event.SmartAction;
import com.smart.smartble.smartBle.BleConfig;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import static com.smart.smartble.event.Action.REQUEST_ACTION_DELETE_DATA_BY_UTC;
import static com.smart.smartble.event.Action.REQUEST_ACTION_GET_DATA_BY_INDEX;

/**
 * @author ARZE
 * @version 创建时间：2017/6/17 18:01
 * @说明 修改于2017.7.7日  中途数据传输失败没有处理蓝牙间隔
 */
public class DataComponent extends AbDataComponent {

    private static final String TAG = "DataComponent";

    public DataComponent(SmartManager smartManager) {
        super(smartManager);
    }

    private Timer mTimer;

    private int mCount = 0;

    private int mCurrentIndex = -1;

    private boolean isStartRequest = false;

    private DataMessageHelper mDataMessageHelper;

    public static final String DEBUG = "DEBUG";

    public long mRequestTime = 0;

    private static final long INTERVAL_TIME = 5 * 60 * 1000;
    private boolean isChangeInterval = false;
    private boolean hasChangeInterval = false;

    @Override
    public void start() {

    }

    @Override
    public void dealAction(SmartAction action) {
        byte[] bytes = action.getBytes();
        switch (action.getAction()) {
            case REQUEST_ACTION_GET_ONE_DAY:
                int sumStep = ((bytes[8] & 0xff) << 8) + (bytes[9] & 0xff);
                dispatchSumStep(sumStep);
                break;
            case REQUEST_ACTION_GET_TARGET:
                int target = ((bytes[8] & 0xff) << 8) + (bytes[9] & 0xff);
                dispatchTarget(target);
                break;
            case REQUEST_ACTION_REPLAY_CHANGE_BLE:
                if (!isChangeInterval)
                    return;
                hasChangeInterval = true;
                int tip = bytes[8] & 0xff;
                int successful = bytes[9] & 0xff;
                if (0 == successful && (0 == tip || 2 == tip)) {
                    getContentIndexData();
                    isChangeInterval = false;
                    BleConfig.WRITE_TIME = 400;
                } else if (0 != successful && 0 == tip) {
                    changeBleInterval(2);
                } else if (0 != successful && 1 == tip) {
                    changeBleInterval(3);
                } else if (0 != successful && 2 == tip) {
                    getContentIndexData();
                }
                break;
            case REQUEST_ACTION_GET_CONTENT:
                int style = ((bytes[8] & 0xff) << 8) + (bytes[9] & 0xff);
                int count = ((bytes[10] & 0xff) << 8) + (bytes[11] & 0xff);
                int sumDataLength = ((bytes[12] & 0xff) << 24) + ((bytes[13] & 0xff) << 16) + ((bytes[14] & 0xff) << 8) + (bytes[15] & 0xff);
                if (null == mDataMessageHelper)
                    return;
                mDataMessageHelper.getmContent().setmDataStyle(style)
                        .setmContentCount(count).setmSumDataLength(sumDataLength);
                Log.w(TAG, "REQUEST_ACTION_GET_CONTENT::" + ByteToString.byteToString(bytes) + " or    count::" + count);
                if (0 == count) {
                    isStartRequest = false;
                }
                if (!mDataMessageHelper.getmContent().isComplete())
                    getContentMessage();
                break;
            case REQUEST_ACTION_GET_CONTENT_MESSAGE:
                ContentMessage contentMessage = dealContentMessage(bytes);
                if (mDataMessageHelper == null) {
                    return;
                }
                if (mDataMessageHelper.getmContent().hasContentMessage(contentMessage))
                    return;
                mDataMessageHelper.getmContent().addContentMessage(contentMessage);
                if (!mDataMessageHelper.getmContent().isComplete()) {
                    getContentMessage();
                } else {
                    if (!mDataMessageHelper.isUploadComplete()) {
                        if (mDataMessageHelper.getmContent().getmSumDataLength() / 16 > 16) {
                            changeBleInterval(0);
                            isChangeInterval = true;
                        } else {
                            getContentIndexData();
                        }
                    }
                }
                break;
            case REQUEST_ACTION_GET_DATA_BY_INDEX:
                Log.w(TAG, "REQUEST_ACTION_REPLAY_DATA::" + ByteToString.byteToString(bytes));

                break;
            case REQUEST_ACTION_REPLAY_DATA:
                if (null == mDataMessageHelper)
                    return;
                Log.w(TAG, "REQUEST_ACTION_REPLAY_DATA::" + ByteToString.byteToString(bytes));
                ContentData contentData = dealContentData(bytes);
                if (null != mDataMessageHelper.getmCurrentMessage()) {
                    if (mDataMessageHelper.getmCurrentMessage().isVailContentData(contentData)) {
                        Log.w(TAG, "REQUEST_ACTION_GET_CONTENT_MESSAGE::" + "add");
                        mDataMessageHelper.getmCurrentMessage().addContentData(contentData);
                    } else {
                        if (mDataMessageHelper.getmCurrentMessage().isLostContentData(contentData)) {
                            Log.w(TAG, "REQUEST_ACTION_GET_CONTENT_MESSAGE::" + "lost");
                            getContentIndexData();
                        }
                    }
                }
                if (mDataMessageHelper.getmCurrentMessage() != null) {
                    if (mDataMessageHelper.getmCurrentMessage ().isComplete () && 0 != mDataMessageHelper.getUploadComplete () && mDataMessageHelper.getUploadComplete () % 16 == 0) {

                        deleteMessageByUtc ();
                        complete ();
                        return;
                    }
                    if (mDataMessageHelper.getmCurrentMessage ().isComplete () && !mDataMessageHelper.isUploadComplete ()) {
                        getContentIndexData ();
                    }
                }
                complete();
                break;
            case REQUEST_ACTION_DELETE_DATA_BY_UTC:
                if(null == mDataMessageHelper){
                    return;
                }
                Log.w(TAG, "REQUEST_ACTION_DELETE_DATA_BY_UTC::" + "delete");
                int utc = ((bytes[10] & 0xff) << 24) + ((bytes[11] & 0xff) << 16) + ((bytes[12] & 0xff) << 8) + (bytes[13] & 0xff);
                int result = (bytes[14] & 0xff);
                Log.w(TAG, "REQUEST_ACTION_DELETE_DATA_BY_UTC::" + "delete" + result);
                if (0 == result) {
                    boolean successfully = mDataMessageHelper.setMessageDelete(utc, true);
                    Log.w(TAG, "REQUEST_ACTION_DELETE_DATA_BY_UTC::" + successfully);
                    if (successfully) {
                        deleteMessageByUtc();
                    }
                }
                break;
        }
    }

    private void complete() {
        if (mDataMessageHelper.isUploadComplete()) {
            isStartRequest = false;
            dispatchRequestComplete(mDataMessageHelper.createWatchBean());
            deleteMessageByUtc();
            BleConfig.WRITE_TIME = 1100;
            Log.w(TAG, "REQUEST_ACTION_DELETE_DATA_BY_UTC::" + mDataMessageHelper.createWatchBean().size());
            if (hasChangeInterval) {
                changeBleInterval(1);
            }
            // mDataMessageHelper.getmContent().reset();
        }
    }

    private void deleteMessageByUtc() {
        Log.w(TAG, "deleteMessageByUtc::");
        ContentMessage deleteMessage = mDataMessageHelper.getDeleteMessage();
        if (null != deleteMessage) {
            Log.w(TAG, "deleteMessageByUtc::" + "send delete" + mDataMessageHelper.getUploadComplete());
            mSmartManager.getIClient().doAction(REQUEST_ACTION_DELETE_DATA_BY_UTC, mDataMessageHelper.getmContent().getmDataStyle(), deleteMessage.getmUtc());
        } else {
            requestContent(DataStyle.STEP);
        }
    }

    private ContentData dealContentData(byte[] bytes) {
        int index = ((bytes[0] & 0xff) << 8) + (bytes[1] & 0xff);
        int style = ((bytes[2] & 0xff) << 8) + (bytes[3] & 0xff);
        return new ContentData().setmIndex(index).setmDataStyle(style).setBytes(bytes);
    }

    private void getContentIndexData() {
        if (!mDataMessageHelper.isUploadComplete()) {
            ContentMessage contentMessage = mDataMessageHelper.getNeedContentMessage();
            if (null != contentMessage) {
                int style = mDataMessageHelper.getmContent().getmDataStyle();
                mSmartManager.getIClient().doAction(REQUEST_ACTION_GET_DATA_BY_INDEX,
                        style, contentMessage.getmUtc(), contentMessage.getCurrentIndex());
            }
        }
    }

    private ContentMessage dealContentMessage(byte[] bytes) {
        int utc = ((bytes[8] & 0xff) << 24) + ((bytes[9] & 0xff) << 16) + ((bytes[10] & 0xff) << 8) + (bytes[11] & 0xff);
        int dataLength = ((bytes[12] & 0xff) << 8) + (bytes[13] & 0xff);
        int perTime = ((bytes[14] & 0xff) << 8) + (bytes[15] & 0xff);
        int mtu = (bytes[16] & 0xff);
        int count = dataLength / ContentMessage.PER_COUNT;
        if (0 != dataLength % ContentMessage.PER_COUNT)
            count += 1;
        ContentMessage contentMessage = new ContentMessage();
        contentMessage.setmDataLength(dataLength).setmUtc(utc).setmPerTime(perTime).setmMtu(mtu).setmCount(count);

        Log.w(TAG, "REQUEST_ACTION_GET_CONTENT_MESSAGE::" + ByteToString.byteToString(bytes) + " or  count::" + count);
        return contentMessage;
    }

    private void getContentMessage() {
        if (null != mDataMessageHelper) {
            mDataMessageHelper.getmContent().getmCurrentIndex();
            int style = mDataMessageHelper.getmContent().getmDataStyle();
            getContentMessageByIndex(style, mDataMessageHelper.getmContent().getmCurrentIndex());
        }
    }

    private void getContentMessageByIndex(int style, int index) {
        mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_GET_CONTENT_MESSAGE, style, index);
    }

    public void requestSumStep() {
        if (null != mTimer)
            mTimer.cancel();
        mTimer = new Timer();

        mTimer.schedule(new Heart(this), 0, 10 * 1000);
        mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_GET_ONE_DAY);
    }

    public void pauseSumStep() {
        if (null != mTimer)
            mTimer.cancel();
    }

    /**
     * 获取数据目录
     *
     * @param style 0000 步数
     *              0001 睡眠
     */
    public void requestContent(DataStyle style) {
        //不在请求数据 或者 时间间隔大于5分钟
        Log.w(TAG, "requestContent:: before" + System.currentTimeMillis() + " or " + mRequestTime);
        if ((isStartRequest && (System.currentTimeMillis() - mRequestTime < INTERVAL_TIME)) || !SmartManager.isDiscovery())
            return;
        isStartRequest = true;
        mRequestTime = System.currentTimeMillis();
        mDataMessageHelper = new DataMessageHelper();
        if (DataStyle.STEP == style) {
            Log.w(TAG, "requestContent::");
            mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_GET_CONTENT, 0);
        } else if (DataStyle.SLEEP == style) {
            mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_GET_CONTENT, 1);
        }
    }

    public void requestDataByIndex(int index) {
        mSmartManager.getIClient().doAction(REQUEST_ACTION_GET_DATA_BY_INDEX, index);
    }

    public void setSportTarget(int target) {
        mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_SET_TARGET, target);
    }

    public void getSportTarget() {
        mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_GET_TARGET);
    }

    public void changeBleInterval(int value) {
        mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_CHANGE_BLE_INTERVAL, value);
    }

    @Override
    public void unRegisterComponent() {
        super.unRegisterComponent();
    }

    private static class Heart extends TimerTask {

        public WeakReference<DataComponent> mTarget;

        public Heart(DataComponent component) {
            mTarget = new WeakReference<>(component);
        }

        @Override
        public void run() {
            if (null != mTarget.get() && mTarget.get().mSmartManager.isDiscovery()) {
                mTarget.get().mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_GET_ONE_DAY);
            }
        }
    }

}
