package com.jason.localgallery.util;

import android.graphics.Bitmap;

import java.util.concurrent.Callable;

/**
 * Created by jason on 2016/8/8.
 *
 */
public interface TaskCaller extends Callable<Bitmap> {

    void onFinish();
    void onError();

}
