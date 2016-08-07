package com.jason.localgallery.util;

import android.os.Handler;
import android.os.HandlerThread;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by jason on 2016/8/6.
 */
public class TaskDistributor {

    public ExecutorService mThreadPool;
    public HandlerThread mHandlerThread;
    public Set<Future> mFutures;
    private static TaskDistributor instance;

    private TaskDistributor() {
        mThreadPool = Executors.newFixedThreadPool(5);
        mHandlerThread = new HandlerThread("download");
        mHandlerThread.start();
        mFutures = new HashSet<>();
    }

    public static TaskDistributor getInstance() {
        if (instance == null) {
            synchronized (TaskDistributor.class) {
                if (instance == null) {
                    instance = new TaskDistributor();
                }
            }
        }
        return instance;
    }

}
