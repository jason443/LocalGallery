package com.jason.localgallery.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by jason on 2016/8/4.
 */
public class ImageLoader {

    private String mUrl;
    private ImageView mImageView;
    private BitmapCacheMaker mBitmapCache;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bitmap bitmap = (Bitmap) msg.obj;
            if (mImageView.getTag().equals(mUrl)) {
                mImageView.setImageBitmap(bitmap);
            }
        }
    };

    public ImageLoader(String url, ImageView imageView) {
        mUrl = url;
        mImageView = imageView;
        mBitmapCache = BitmapCacheMaker.getInstance();
    }

    public void loadImage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = mBitmapCache.getBitmapFromLruCache(mUrl);
                if (bitmap != null) {
                    Message message = Message.obtain();
                    message.obj = bitmap;
                    mHandler.sendMessage(message);
                } else {
                    bitmap = getBitmapFromURL(mUrl);
                    mBitmapCache.addBitmapToLruCache(mUrl, bitmap);
                    Message message = Message.obtain();
                    message.obj = bitmap;
                    mHandler.sendMessage(message);
                }
            }
        }).start();
    }

    private Bitmap getBitmapFromURL(String urlString) {
        Bitmap bitmap;
        InputStream is = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(connection.getInputStream());
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            bitmap = BitmapFactory.decodeStream(is);
          //  bitmap = PictureProcessor.compressBitmapFromStream(is, 100, 100); // 压缩图片
            connection.disconnect();
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
