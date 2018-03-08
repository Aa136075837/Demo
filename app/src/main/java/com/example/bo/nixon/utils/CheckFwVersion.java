package com.example.bo.nixon.utils;

/**
 * @author ARZE
 * @version 创建时间：2016/11/10 17:30
 * @说明
 */
public class CheckFwVersion {

    private static final int GET_URL = 0x0001;
    private static final int GET_MESSAGE = 0x0002;
    private static final int NO_VERSION = 0x0003;
  //  private VersionListener mVersionListener;
    private String mUrl;

   /* private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_MESSAGE:
                    if (null != mVersionListener && msg.obj instanceof String) {
                        mVersionListener.onSucc(mUrl, (String) msg.obj);
                    }
                    break;
                case NO_VERSION:
                    if (null != mVersionListener ) {
                        mVersionListener.onNoVersion();
                    }
                    break;
            }
        }
    };

    public void checkVersion(final String version,String information) {
        if (TextUtils.isEmpty(version))
            return;
        String test = information;
        if (test.length() > 60) {
            int i = test.indexOf("FF");
            String keyAscii = test.substring(i + 2, i + 12);
            String key = AsciiToString.ascii16ToString(keyAscii);
            if ("P4001".equals(key)) {
                key = "M" + key;
            } else if ("P1003 ".equals(key)) {
                key = "P0062";
            }
            final String var1 = key;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = getReallyFileName("http://smartmovt.net/update-ver.php?sn=" + var1 + "&ver=" + version, 0);
                    if (!TextUtils.isEmpty(url)) {
                        if (url.substring(url.length() - 4, url.length()).equals(".zip")) {
                            mUrl = url;
                            getVersionMessage(var1, version);
                        } else {
                            mHandler.sendEmptyMessage(NO_VERSION);
                        }
                    }
                }
            }).start();
        }
    }

    public static synchronized String getReallyFileName(String url, int o) {
        String filename = "";
        URL myURL;
        HttpURLConnection conn = null;
        if (url == null || url.length() < 1) {
            return null;
        }
        try {
            myURL = new URL(url);
            conn = (HttpURLConnection) myURL.openConnection();
            conn.connect();
            conn.getResponseCode();
            URL absUrl = conn.getURL();// 获得真实Url
            if (o == 0) {
                return absUrl.toString();
            } else {
                filename = conn.getHeaderField("Content-Disposition");
                if (filename == null || filename.length() < 1) {
                    filename = absUrl.getFile();
                }
                return filename;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return null;
    }

    private void getVersionMessage(String sn, String oldVersion) {
        if (null != oldVersion && oldVersion.length() > 1) {
            new Thread(new AccessNetwork("GET", "http://smartmovt.net/verlog.php?type=oat&sn=" + sn + "&ver="
                    + oldVersion.substring(1, oldVersion.length()), null, mHandler, true)).start();
        }
    }

    private class AccessNetwork implements Runnable {
        private String op;
        private String url;
        private String params;
        private Handler h;
        private boolean isfw;

        public AccessNetwork(String op, String url, String params, Handler h, boolean isfwof) {
            super();
            this.op = op;
            this.url = url;
            this.params = params;
            this.h = h;
            this.isfw = isfwof;
        }

        @Override
        public void run() {
            Message m = new Message();
            m.what = GET_MESSAGE;
            if (op.equals("GET")) {
                m.obj = GetPostUtil.sendGet(url, params);
            }
            if (op.equals("POST")) {
                m.obj = GetPostUtil.sendPost(url, params);
            }
            h.sendMessage(m);
        }
    }

    public interface VersionListener {
        void onSucc(String url, String message);

        void onNoVersion();
    }

    public void setVersionListener(VersionListener listener) {
        mVersionListener = listener;
    }

    public void downLoaderTask(String url, Context context) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/OTA/");
        FileUtil.DeleteFile(file);
        DownLoaderTask task = new DownLoaderTask(url,
                Environment.getExternalStorageDirectory().getAbsolutePath() + "/OTA/", context);
        task.execute();
    } */

}
