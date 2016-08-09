package com.jason.localgallery.util;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by jason on 2016/8/9.
 *
 */
public class LargeBitmapCacheMaker {
    
    private static LruCache<String, Bitmap> mLruCacheBitmap;
    private static LargeBitmapCacheMaker instance;
    
    private LargeBitmapCacheMaker() {
        final int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory/8;
        mLruCacheBitmap = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }
    
    public static LargeBitmapCacheMaker getInstance() {
        if (instance == null) {
            synchronized (LargeBitmapCacheMaker.class) {
                if (instance == null) {
                    instance = new LargeBitmapCacheMaker();
                }
            }
        }
        return instance;
    }

    public synchronized void addBitmapToLruCache(String url, Bitmap bitmap) {
        if (bitmap != null) {
            mLruCacheBitmap.put(url, bitmap);
        }
    }

    public Bitmap getBitmapFromLruCache(String url) {
        return mLruCacheBitmap.get(url);
    }
    
}
