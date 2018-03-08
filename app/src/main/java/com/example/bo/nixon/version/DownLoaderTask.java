
package com.example.bo.nixon.version;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownLoaderTask extends AsyncTask<Void, Integer, Long> {

    private final String TAG = "DownLoaderTask";

    private URL mUrl;

    private File mFile;

    private int mProgress = 0;

    private ProgressReportingOutputStream mOutputStream;

    private LoaderResultListener loaderResultListener;

    private long mCurrentLength;


    public DownLoaderTask(String url, String out) {
        super();
        try {
            mFile = new File(out);
            if (!mFile.exists()) {
                mFile.mkdir();
            }
            mUrl = new URL(url);
            String fileName = new File(mUrl.getFile()).getName();
            mFile = new File(out, fileName);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPreExecute() {
    }

    @Override

    protected Long doInBackground(Void... params) {
        return download();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if (values.length > 1) {
            mCurrentLength = values[1];
        } else {
            Log.w("DownLoaderTask", "onProgressUpdate：：：" + values[0].intValue());
            if (null != loaderResultListener) {
                loaderResultListener.onVersionProgress(mCurrentLength, values[0].intValue());
            }
        }
    }

    @Override
    protected void onPostExecute(Long result) {
        Log.w("DownLoaderTask", "onPostExecute：：：" + result);
        try {
            mOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (null != loaderResultListener) {
            loaderResultListener.onVersionResult(result == mCurrentLength,mFile.getAbsolutePath());
        }
    }

    private long download() {

        URLConnection connection = null;
        int bytesCopied = 0;
        try {
            connection = mUrl.openConnection();
            int length = connection.getContentLength();
            if (mFile.exists() && length == mFile.length()) {
                return 0l;
            }
            mOutputStream = new ProgressReportingOutputStream(mFile);
            publishProgress(0, length);
            bytesCopied = copy(connection.getInputStream(), mOutputStream);
            if (bytesCopied != length && length != -1) {
            }
            mOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytesCopied;

    }

    private int copy(InputStream input, OutputStream output) {
        byte[] buffer = new byte[1024 * 8];
        BufferedInputStream in = new BufferedInputStream(input, 1024 * 8);
        BufferedOutputStream out = new BufferedOutputStream(output, 1024 * 8);
        int count = 0, n = 0;
        try {
            while ((n = in.read(buffer, 0, 1024 * 8)) != -1) {
                out.write(buffer, 0, n);
                count += n;
            }
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    private final class ProgressReportingOutputStream extends FileOutputStream {

        public ProgressReportingOutputStream(File file)
                throws FileNotFoundException {
            super(file);
        }

        @Override
        public void write(byte[] buffer, int byteOffset, int byteCount)
                throws IOException {
            super.write(buffer, byteOffset, byteCount);
            mProgress += byteCount;
            publishProgress(mProgress);
        }
    }

    public interface LoaderResultListener {

        void onVersionProgress(long max, long progress);

        void onVersionResult(boolean complete,String name);
    }

    public void setLoaderResultListener(LoaderResultListener loaderResultListener) {
        this.loaderResultListener = loaderResultListener;
    }
}
