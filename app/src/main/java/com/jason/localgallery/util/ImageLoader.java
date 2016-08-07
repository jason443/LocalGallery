package com.jason.localgallery.util;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;

import com.jason.localgallery.bean.BitmapBean;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * Created by jason on 2016/8/4.
 */
public class ImageLoader {

    private List<String> mUrls;
    private GridView mGridView;
    private BitmapCacheMaker mBitmapCache;
    private TaskDistributor mTaskDistributor;
    private ExecutorService mThreadPool;
    private Set<Future> mFutures;
    private Handler mTaskHandler;
    private HandlerThread mHandlerThread;
    private Handler mUIHandler;

    public ImageLoader(List<String> urls, GridView gridView) {
        mUrls = urls;
        mGridView = gridView;
        mFutures = new HashSet<>();
        mUIHandler = new Handler();
        mGridView = gridView;
        mBitmapCache = BitmapCacheMaker.getInstance();
        mTaskDistributor = TaskDistributor.getInstance();
        mThreadPool = mTaskDistributor.mThreadPool;
        mHandlerThread = mTaskDistributor.mHandlerThread;
        mTaskHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                BitmapBean bitmapBean = (BitmapBean) msg.obj;
                final String url = bitmapBean.getmUrl();
                final Bitmap bitmap = bitmapBean.getmBitmap();
                if (url != null && bitmap != null) {
                    mBitmapCache.addBitmapToLruCache(url, bitmap);
                }
                mUIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ImageView imageView = (ImageView) mGridView.findViewWithTag(url);
                        if (bitmap != null && imageView != null) {
                            imageView.setImageBitmap(bitmap);
                        }
                    }
                });
            }
        };
    }

    public void loadImages(int startItem, int endItem) {
        for (int i = startItem; i < endItem; i++) {
            final int index = i;
            final String url = mUrls.get(index);
            Bitmap bitmap = mBitmapCache.getBitmapFromLruCache(url);
            if (bitmap != null) {
                ImageView imageView = (ImageView) mGridView.findViewWithTag(url);
                imageView.setImageBitmap(bitmap);
            } else {
                Future future = mThreadPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        //执行下载逻辑
                        Bitmap bitmap = getBitmapFromURL(url);
                        BitmapBean bitmapBean = new BitmapBean(url, bitmap);
                        Message message = Message.obtain();
                        message.obj = bitmapBean;
                        mTaskHandler.sendMessage(message);
                    }
                });
                mFutures.add(future);
            }
        }
    }

    public void cancelAllTask() {
        if (mFutures.size() > 0) {
            for (Future future : mFutures) {
                future.cancel(true);
            }
            mFutures.clear();
        }
    }

    private Bitmap getBitmapFromURL(String urlString) {
        Bitmap bitmap;
        BufferedInputStream is = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            is = new BufferedInputStream(connection.getInputStream());
            byte[] bytes = getBytesFromInputStream(is);
            bitmap = new ImageProcessor().compressBitmapFromBytes(bytes, 100, 100);
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

    public byte[] getBytesFromInputStream(InputStream is) {
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = -1;
                while ((len = is.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                outputStream.close();
                return outputStream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
    }

}
