package com.jason.localgallery.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.jason.localgallery.R;
import com.jason.localgallery.adapter.ImageFragAdapter;
import com.jason.localgallery.application.CustomApplication;
import com.jason.localgallery.fragment.LargeImageFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 2016/8/9.
 *
 */
public class LargeImageActivity extends FragmentActivity {

    private ViewPager mVpShow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_large_image);
        init();
        int index = getIntent().getIntExtra("index", 0);
        mVpShow.setCurrentItem(index);
    }

    public void init() {
        CustomApplication application = (CustomApplication) getApplication();
        String[] imageUrls = application.getImageUrls();
        mVpShow = (ViewPager) findViewById(R.id.large_image_vp_show);
        List<LargeImageFragment> fragments = new ArrayList<>();
        for (String url : imageUrls) {
            fragments.add(LargeImageFragment.newInstance(url));
        }
        ImageFragAdapter imageFragAdapter = new ImageFragAdapter(getSupportFragmentManager(), fragments);
        mVpShow.setAdapter(imageFragAdapter);
    }
}
