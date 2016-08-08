package com.jason.localgallery.bean;

import android.graphics.Bitmap;

/**
 * Created by jason on 2016/8/8.
 *
 */
public class UIHandlerBean {

    private Bitmap bitmap;
    private String url;
    private int index;

    public UIHandlerBean(Bitmap bitmap, String url, int index) {
        this.bitmap = bitmap;
        this.url = url;
        this.index = index;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getIndex() {
        return index;
    }

    public String getUrl() {
        return url;
    }
}
