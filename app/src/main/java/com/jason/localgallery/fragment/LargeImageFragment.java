package com.jason.localgallery.fragment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.jason.localgallery.R;
import com.jason.localgallery.util.LargeBitmapCacheMaker;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jason on 2016/8/9.
 *
 */
public class LargeImageFragment extends Fragment implements View.OnClickListener {

    public static final String KEY_URL = "url";
    private String mUrl;
    private ImageView mIvShowImage;
    private Button mBtBack;
    private LargeBitmapCacheMaker mLargeBitmapCache;

    public static LargeImageFragment newInstance(String url) {
        LargeImageFragment fragment = new LargeImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_URL, url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLargeBitmapCache = LargeBitmapCacheMaker.getInstance();
        mUrl = getArguments().getString(KEY_URL);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.large_image_fragment, container, false);
        mIvShowImage = (ImageView) view.findViewById(R.id.fragment_iv_show);
        mIvShowImage.setOnClickListener(this);
        mBtBack = (Button) view.findViewById(R.id.fragment_bt_back);
        mBtBack.setOnClickListener(this);
        Bitmap bitmap = mLargeBitmapCache.getBitmapFromLruCache(mUrl);
        if (bitmap != null) {
            mIvShowImage.setImageBitmap(bitmap);
            return view;
        }
        mIvShowImage.setImageResource(R.mipmap.ic_launcher); // 设置默认图片
        new ImageAsyncTack().execute(mUrl); // 开启网络下载
        return view;
    }

    private Bitmap getBitmapFromUrl(String urlString) {
        Bitmap bitmap;
        BufferedInputStream is = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(3000);
            connection.setConnectTimeout(3000);
            is = new BufferedInputStream(connection.getInputStream());
            bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_iv_show:
                if (mBtBack.getVisibility() == View.VISIBLE) {
                    mBtBack.setVisibility(View.INVISIBLE);
                } else {
                    mBtBack.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.fragment_bt_back:
                getActivity().finish();
                break;
        }
    }

    class ImageAsyncTack extends AsyncTask<String, Void, Bitmap> {

        private String url;

        @Override
        protected Bitmap doInBackground(String... params) {
            url = params[0];
            return getBitmapFromUrl(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null && url != null) {
                mLargeBitmapCache.addBitmapToLruCache(url, bitmap);
                mIvShowImage.setImageBitmap(bitmap);
            }
        }
    }
}
