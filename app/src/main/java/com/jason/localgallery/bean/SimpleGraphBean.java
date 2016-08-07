package com.jason.localgallery.bean;

/**
 * Created by jason on 2016/8/4.
 */
public class SimpleGraphBean {

    private String bitmapUrl;

    public SimpleGraphBean(String bitmapUrl) {
        this.bitmapUrl = bitmapUrl;
    }

    public String getBitmapUrl() {
        return bitmapUrl;
    }

    public void setBitmapUrl(String bitmapUrl) {
        this.bitmapUrl = bitmapUrl;
    }
}
