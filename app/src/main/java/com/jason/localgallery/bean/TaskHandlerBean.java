package com.jason.localgallery.bean;

import com.jason.localgallery.util.LIFOTask;

/**
 * Created by jason on 2016/8/8.
 *
 */
public class TaskHandlerBean {

    private LIFOTask mTask;
    private int mIndex;

    public TaskHandlerBean(int mIndex, LIFOTask mTask) {
        this.mIndex = mIndex;
        this.mTask = mTask;
    }

    public LIFOTask getTask() {
        return mTask;
    }

    public int getIndex() {
        return mIndex;
    }
}
