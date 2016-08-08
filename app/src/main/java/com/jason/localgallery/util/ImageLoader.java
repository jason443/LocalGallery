package com.jason.localgallery.util;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.widget.GridView;
import android.widget.ImageView;

import com.jason.localgallery.bean.UIHandlerBean;
import com.jason.localgallery.bean.TaskHandlerBean;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jason on 2016/8/7.
 *
 */
public class ImageLoader {

    private static ImageLoader instance;
    private LIFOThreadPoolProcessor mPoolProcessor;
    private List<String> mUrls;
    private Map<Integer, LIFOTask> mTasks;
    private GridView mGridView;
    private BitmapCacheMaker mLruCache;
    public static final int FINISH_RESPONSE = 0;
    public static final int ERROR_RESPONSE = 1;
    private UIHandler mUIHandler;
    private TaskHandler mTaskHandler;

    private ImageLoader(GridView gridView, List<String> urls) {
        init(gridView, urls);
    }

    public void init(GridView gridView, List<String> urls) {
        mPoolProcessor = new LIFOThreadPoolProcessor(5);
        HandlerThread handlerThread = new HandlerThread("download");
        handlerThread.start();
        mTaskHandler = new TaskHandler(handlerThread.getLooper());
        mUIHandler = new UIHandler();
        mTasks = new HashMap<>();
        mGridView = gridView;
        mUrls = urls;
        mLruCache = BitmapCacheMaker.getInstance();
    }

    public static ImageLoader getInstance(GridView gridView, List<String> urls) {
        if (instance == null) {
            synchronized (ImageLoader.class) {
                if (instance == null) {
                    instance = new ImageLoader(gridView, urls);
                }
            }
        }
        return instance;
    }

    public void cancelAllTask() {
        if (mTasks.size() > 0) {
            for(LIFOTask task : mTasks.values()) {
                task.cancel(true);
            }
            mTasks.clear();
            mPoolProcessor.clean();
        }
    }

    public void loadImages(int startItem, int endItem) {
        for (int i = startItem; i<endItem ; i++) {
            final int index = i;
            final String url = mUrls.get(index);
            Bitmap bitmap = mLruCache.getBitmapFromLruCache(url);
            if(bitmap != null) {
                ImageView imageView = (ImageView) mGridView.findViewWithTag(url);
                if(imageView != null) {
                    imageView.setImageBitmap(bitmap);
                    continue;
                }
            }
            LIFOTask task = new LIFOTask(new TaskCaller() {

                private UIHandlerBean mUIHandlerBean;

                @Override
                public void onFinish() {
                    Message message = Message.obtain();
                    message.what = FINISH_RESPONSE;
                    message.obj = mUIHandlerBean;
                    mUIHandler.sendMessage(message);
                }

                @Override
                public void onError() {

                }

                @Override
                public Bitmap call() throws Exception {
                    Bitmap bitmap = getBitmapFromURL(url);
                    if (bitmap != null) {
                        mUIHandlerBean = new UIHandlerBean(bitmap, url, index);
                        mLruCache.addBitmapToLruCache(url, bitmap);
                        onFinish();
                        return bitmap;
                    }
                    onError();
                    return null;
                }
            });
            TaskHandlerBean taskBean = new TaskHandlerBean(index, task);
            Message message = Message.obtain();
            message.obj = taskBean;
            mTaskHandler.sendMessage(message);
        }
    }

    /**
     * 通过图片url获取从网络获取图片
     * @param urlString 图片的url
     * @return 从网络获取的经过压缩后的Bitmap
     */
    private Bitmap getBitmapFromURL(String urlString) {
        Bitmap bitmap;
        BufferedInputStream is = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            is = new BufferedInputStream(connection.getInputStream());
            byte[] bytes = getBytesFromInputStream(is);
            bitmap = new ImageProcessor().compressBitmapFromBytes(bytes, 100, 100);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
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

    /**
     * 将inputStream转化为包含Bitmap信息的byte数组
     * @param is 传入的inputStream
     * @return 返回的bytes数组
     */
    public byte[] getBytesFromInputStream(InputStream is) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
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

    class TaskHandler extends Handler {

        public TaskHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            TaskHandlerBean taskBean = (TaskHandlerBean) msg.obj;
            mTasks.put(taskBean.getIndex(), taskBean.getTask());
            mPoolProcessor.submitTask(taskBean.getTask());
        }
    }

    class UIHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FINISH_RESPONSE:
                    UIHandlerBean handlerBean = (UIHandlerBean) msg.obj;
                    mTasks.remove(handlerBean.getIndex());
                    Bitmap bitmap = handlerBean.getBitmap();
                    String url = handlerBean.getUrl();
                    ImageView imageView = (ImageView) mGridView.findViewWithTag(url);
                    if (imageView!= null && bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    }
                    break;
                case ERROR_RESPONSE:
                    break;
                default:
            }
        }
    }

}
