package com.jason.localgallery.util;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by jason on 2016/8/4.
 *
 */
public class BitmapCacheMaker {

    private static LruCache<String, Bitmap> mLruCacheBitmap;

    private static BitmapCacheMaker instance;

    private BitmapCacheMaker() {
        final int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        mLruCacheBitmap = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    public static BitmapCacheMaker getInstance() {
        if (instance == null) {
            synchronized (BitmapCacheMaker.class) {
                if (instance == null) {
                    instance = new BitmapCacheMaker();
                }
            }
        }
        return instance;
    }

    /** 从缓存中取出数据
     * @param key 索引
     * @return 从缓存中取得的Bitmap
     */
    public Bitmap getBitmapFromLruCache(String key) {
        return mLruCacheBitmap.get(key);
    }

    /** 将数据增加进缓存
     * @param key 要添加数据的键
     * @param value 要添加数据的值
     */
    public synchronized void addBitmapToLruCache(String key, Bitmap value) {
        if (getBitmapFromLruCache(key) == null) {
            mLruCacheBitmap.put(key, value);
        }
    }

}
