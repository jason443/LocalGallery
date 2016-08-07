package com.jason.localgallery.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.jason.localgallery.R;
import com.jason.localgallery.bean.SimpleGraphBean;
import com.jason.localgallery.util.BitmapCacheMaker;
import com.jason.localgallery.util.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 2016/8/4.
 */
public class SimpleGridViewAdapter extends BaseAdapter implements AbsListView.OnScrollListener {

    private List<SimpleGraphBean> mSimpleGraphBeans;
    private LayoutInflater mInflater;
    private int mStartItem;
    private int mEndItem;
    private List<String> mUrls;
    private ImageLoader mImageLoader;
    private boolean flags = true;

    public SimpleGridViewAdapter(List<SimpleGraphBean> data, Context context, GridView gridView) {
        mSimpleGraphBeans = data;
        mInflater = LayoutInflater.from(context);
        mUrls = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            mUrls.add(mSimpleGraphBeans.get(i).getBitmapUrl());
        }
        mImageLoader = new ImageLoader(mUrls, gridView);
        gridView.setOnScrollListener(this);
    }

    @Override
    public Object getItem(int position) {
        return mSimpleGraphBeans.get(position);
    }

    @Override
    public int getCount() {
        return mSimpleGraphBeans.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SimpleGraphBean simpleGraphBean = (SimpleGraphBean) getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.main_gv_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mIvShowGraph = (ImageView) convertView.findViewById(R.id.item_iv_show);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String url = simpleGraphBean.getBitmapUrl();
        viewHolder.mIvShowGraph.setTag(url);
        Bitmap bitmap = BitmapCacheMaker.getInstance().getBitmapFromLruCache(url);
        if (bitmap != null) {
            viewHolder.mIvShowGraph.setImageBitmap(bitmap);
        } else {
            viewHolder.mIvShowGraph.setImageResource(R.mipmap.ic_launcher);
        }
        return convertView;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            // 开始加载
            mImageLoader.loadImages(mStartItem, mEndItem);
        } else {
            // 停止所有下载任务
            mImageLoader.cancelAllTask();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mStartItem = firstVisibleItem;
        mEndItem = firstVisibleItem + visibleItemCount;
        if (flags && visibleItemCount > 0) { //第一次启动预加载
            mImageLoader.loadImages(mStartItem, mEndItem);
            flags = false;
        }
    }

    class ViewHolder {
        ImageView mIvShowGraph;
    }
}
