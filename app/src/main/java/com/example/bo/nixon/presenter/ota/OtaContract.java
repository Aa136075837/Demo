package com.example.bo.nixon.presenter.ota;

import android.content.ComponentName;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.manager.ToastManager;
import com.example.bo.nixon.presenter.BaseNixonView;
import com.example.bo.nixon.presenter.bleBase.BaseBlePresenter;
import com.example.bo.nixon.version.DownLoaderTask;
import com.example.bo.nixon.version.FileUtil;
import com.example.bo.nixon.version.VersionControl;
import com.example.bo.nixon.version.ZIP;
import com.smart.attributescomponent.AttributesComponent;
import com.smart.attributescomponent.listener.IVersion;
import com.smart.connectComponent.ConnectComponent;
import com.smart.connectComponent.IConnect;
import com.smart.otacomponent.OTAComponent;
import com.smart.otacomponent.listener.IOTAListener;
import com.smart.smartble.SmartManager;
import com.smart.smartble.smartBle.BleDevice;

import java.io.File;

/**
 * @author ARZE
 * @version 创建时间：2017/6/21 14:27
 * @说明
 */
public interface OtaContract {

    interface OtaNixonView extends BaseNixonView {

        void onBegin ();

        void onProgress (int max, int progress);

        void onComplete (boolean complete);

        void onFail ();

        void findFwVersion (String url, String message);

        void noVersion ();

        void showFwVersion (int main, int minor, int test);

    }

    class OtaPresenter extends BaseBlePresenter<OtaNixonView> implements IOtaPresenter,
            IOTAListener, VersionControl.VersionListener, IVersion, DownLoaderTask
                    .LoaderResultListener, IConnect {

        private static final String TAG = "OtaPresenter";
        private static final String PATH = Environment.getExternalStorageDirectory ()
                .getAbsolutePath () + "/OTA/";
        private VersionControl mVersionControl;
        private AttributesComponent mAttributesComponent;
        private DownLoaderTask mDownLoaderTask;
        private OTAComponent mOtaComponent;
        private ConnectComponent mConnectComponent;

        @Override
        public void attachView (OtaNixonView view) {
            super.attachView (view);
            mVersionControl = new VersionControl ();
            mVersionControl.setVersionListener (this);
        }

        @Override
        protected void serviceConnected (SmartManager smartManager) {
            mAttributesComponent = AttributesComponent.getInstance (smartManager);
            mAttributesComponent.registerComponent ();
            mAttributesComponent.addVersionListener (this);
            mAttributesComponent.getVersion ();

            mOtaComponent = OTAComponent.getInstance (smartManager);
            mOtaComponent.addOTAListener (this);
            mOtaComponent.registerComponent ();
            mOtaComponent.setStyle (OTAComponent.Style.NORMAL);

            mConnectComponent = ConnectComponent.getInstance (smartManager);
            mConnectComponent.addConnectListener (this);
        }

        @Override
        protected void serviceDisconnected (ComponentName name) {

        }

        public void checkoutVersion (String version) {
            if (null != mVersionControl) {
                mVersionControl.checkVersion (version, "P7003");
            }
        }

        public void downLoad (String url) {
            Log.w ("DownLoaderTask", "run--------->1" + url);
            if (TextUtils.isEmpty (url))
                return;
            Log.w ("DownLoaderTask", "run--------->2" + url);
            File file = new File (Environment.getExternalStorageDirectory ().getAbsolutePath () +
                    "/OTA/");
            FileUtil.DeleteFile (file);
            mDownLoaderTask = new DownLoaderTask (url, PATH);
            mDownLoaderTask.setLoaderResultListener (this);
            mDownLoaderTask.execute ();
        }

        @Override
        public void detachView () {
            super.detachView ();
            if (null != mAttributesComponent) {
                mAttributesComponent.removeVersionListener (this);
                mAttributesComponent.unRegisterComponent ();
            }
            if (null != mOtaComponent) {
                mOtaComponent.removeOTAListener (this);
                mOtaComponent.setStyle (OTAComponent.Style.SLOW);
            }
            if (null != mAttributesComponent) {
                mConnectComponent.removeConnectListener (this);
            }
        }

        @Override
        public void startOTA (String path) {
            if (null != mOtaComponent) {
                mOtaComponent.startPath (path);
            }
        }

        @Override
        public void dealFile () {
            Log.w (TAG, "dealFile::");
        }

        @Override
        public void otaStart () {
            Log.w (TAG, "otaStart::");
            if (null != getView ())
                getView ().onBegin ();
        }

        @Override
        public void onProgress (int max, int progress) {
            Log.w ("OTAComponent", "onProgress::" + max + "   or   " + progress);
            if (null != getView ())
                getView ().onProgress (max, progress);
        }


        @Override
        public void onComplete (int complete) {
            Log.w (TAG, "onComplete::");
            if (0 == complete) {
                ToastManager.show (NixonApplication.getContext (), "升级成功", Toast.LENGTH_SHORT);
            } else if (5 == complete) {
                ToastManager.show (NixonApplication.getContext (), "升级版本与当前版本一致请更新", Toast
                        .LENGTH_SHORT);
            } else {
                ToastManager.show (NixonApplication.getContext (), "升级中断", Toast.LENGTH_SHORT);
            }
            if (null != getView ())
                getView ().onComplete (0 == complete);
        }

        @Override
        public void onFail () {
            Log.w (TAG, "onFail::");
        }

        @Override
        public void onSuccessful (String url, String message) {
            Log.w (TAG, "url::" + url);
            if (null != getView ()) {
                getView ().findFwVersion (url, message);
            }
        }

        @Override
        public void onNoVersion () {
            if (null != getView ()) {
                getView ().noVersion ();
            }
        }

        @Override
        public void version (int main, int minor, int test) {
            Log.e ("DownLoaderTask", " ABoutFragment  version");
            if (null != getView ()) {
                getView ().showFwVersion (main, minor, test);
                StringBuffer buffer = new StringBuffer ();
                buffer.append ("V").append (main).append (".").append (minor).append (".").append
                        (test);
                checkoutVersion (buffer.toString ());
            }
        }

        @Override
        public void onVersionProgress (long max, long progress) {

        }

        @Override
        public void onVersionResult (boolean complete, String path) {
            File file = new File (path);
            if (null != file)
                if (file.getName ().endsWith ("zip")) {
                    try {
                        ZIP.UnZipFolder (file.getAbsolutePath (), file.getParent (), new ZIP
                                .ZipResult () {
                            @Override
                            public void complete (String path) {
                                startOTA (path);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace ();
                    }
                }
        }

        @Override
        public void findNewDevice (BleDevice bleDevice) {

        }

        @Override
        public void leSanEnd () {

        }

        @Override
        public void leSanStart () {

        }

        @Override
        public void connectFail () {

        }

        @Override
        public void connectedDevice () {
            if (null != mAttributesComponent)
                mAttributesComponent.getVersion ();
        }

        @Override
        public void connectSuccessful () {

        }

    }


}
