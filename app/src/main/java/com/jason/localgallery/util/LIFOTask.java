package com.jason.localgallery.util;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Created by jason on 2016/8/7.
 *
 */
public class LIFOTask extends FutureTask<Bitmap> implements Comparable<LIFOTask> {

    private static long counter = 0;
    private long priority;
    public Callable<Bitmap> mCallable;

    public LIFOTask(Callable<Bitmap> callable) {
        super(callable);
        mCallable = callable;
        priority = counter++;
    }

    public int compareTo(@NonNull LIFOTask task) {
        return priority > task.priority ? -1 : 1;
    }

}
