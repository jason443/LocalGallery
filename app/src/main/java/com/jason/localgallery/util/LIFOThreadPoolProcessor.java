package com.jason.localgallery.util;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by jason on 2016/8/7.
 *
 */
public class LIFOThreadPoolProcessor {

    private ThreadPoolExecutor mThreadPool;

    public LIFOThreadPoolProcessor(int threadCount) {
        BlockingQueue<Runnable> mQueue = new PriorityBlockingQueue<>(80, new Comparator<Runnable>() {
            @Override
            public int compare(Runnable lhs, Runnable rhs) {
                if (lhs instanceof LIFOTask && rhs instanceof LIFOTask) {
                    LIFOTask l1 = (LIFOTask) lhs;
                    LIFOTask l2 = (LIFOTask) rhs;
                    return l1.compareTo(l2);
                }
                return 0;
            }
        });

        mThreadPool = new ThreadPoolExecutor(threadCount, threadCount, 0, TimeUnit.SECONDS, mQueue);
    }

    public Future<?> submitTask(LIFOTask task) {
        return mThreadPool.submit(task);
    }

    public void clean() {
        mThreadPool.purge();
    }

}
