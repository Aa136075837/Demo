package com.example.bo.nixon.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ARZE
 * @version 创建时间：2016/12/6 10:59
 * @说明
 */
public class ThreadPoolManager {

    private static ExecutorService mService;

    static {
        mService = Executors.newCachedThreadPool();
    }

    private ThreadPoolManager () {

    }

    private static ThreadPoolManager mManager;

    public static ThreadPoolManager getInstance() {
        if (null == mManager) {
            synchronized (ThreadPoolManager.class) {
                if (null == mManager)
                    mManager = new ThreadPoolManager();
            }
        }
        return mManager;
    }

    public  void execute(Runnable runnable) {
        mService.execute(runnable);
    }


}
