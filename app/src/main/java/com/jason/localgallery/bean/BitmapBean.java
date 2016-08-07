package com.jason.localgallery.bean;

import android.graphics.Bitmap;

/**
 * Created by jason on 2016/8/6.
 */
public class BitmapBean {

    private String mUrl;
    private Bitmap mBitmap;

    public BitmapBean(String mUrl, Bitmap mBitmap) {
        this.mUrl = mUrl;
        this.mBitmap = mBitmap;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }
}
