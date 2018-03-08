package com.example.bo.nixon.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;

/**
 * @author bo.
 * @Date 2017/9/7.
 * @desc
 */

public class CacheUtils {
    private static Context mContext;

    private CacheUtils () {
    }

    static class SingleInstance {
        static CacheUtils mCacheUtils = new CacheUtils ();
    }

    public static CacheUtils getInstance (Context context) {
        mContext = context;
        return SingleInstance.mCacheUtils;
    }

    public void clearCache () {
        File cacheDir = mContext.getCacheDir ();
        Log.e ("CacheUtils"," 文件大小 == " + getDirSize(cacheDir));
        cacheDir.delete ();
    }

    public long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file); // 递归调用继续统计
            }
        }
        return dirSize;
    }

}
