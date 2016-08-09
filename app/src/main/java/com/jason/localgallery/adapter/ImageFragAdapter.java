package com.jason.localgallery.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.jason.localgallery.fragment.LargeImageFragment;

import java.util.List;

/**
 * Created by jason on 2016/8/9.
 *
 */
public class ImageFragAdapter extends FragmentStatePagerAdapter {

    private List<LargeImageFragment> mFragments;

    public ImageFragAdapter(FragmentManager fm, List<LargeImageFragment> mFragments) {
        super(fm);
        this.mFragments = mFragments;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public LargeImageFragment getItem(int position) {
        return mFragments.get(position);
    }
}
