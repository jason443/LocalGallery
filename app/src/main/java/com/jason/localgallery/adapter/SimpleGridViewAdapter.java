package com.jason.localgallery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jason.localgallery.R;
import com.jason.localgallery.bean.SimpleGraphBean;
import com.jason.localgallery.util.ImageLoader;

import java.util.List;

/**
 * Created by jason on 2016/8/4.
 */
public class SimpleGridViewAdapter extends BaseAdapter {

    private List<SimpleGraphBean> mSimpleGraphBeans;
    private LayoutInflater mInflater;

    public SimpleGridViewAdapter(List<SimpleGraphBean> mSimpleGraphBeans, Context context) {
        this.mSimpleGraphBeans = mSimpleGraphBeans;
        this.mInflater = LayoutInflater.from(context);
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
        viewHolder.mIvShowGraph.setTag(simpleGraphBean.getBitmapUrl());
        viewHolder.mIvShowGraph.setImageResource(R.mipmap.ic_launcher);
        new ImageLoader(simpleGraphBean.getBitmapUrl(), viewHolder.mIvShowGraph).loadImage();
        return convertView;
    }

    class ViewHolder {
        ImageView mIvShowGraph;
    }
}
