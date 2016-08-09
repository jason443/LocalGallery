package com.jason.localgallery.application;

import android.app.Application;

/**
 * Created by jason on 2016/8/9.
 *
 */
public class CustomApplication  extends Application {

    private String[] mImageUrls;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void setImageUrls(String[] imageUrls) {
        mImageUrls = imageUrls;
    }

    public String[] getImageUrls() {
        return mImageUrls;
    }
}
