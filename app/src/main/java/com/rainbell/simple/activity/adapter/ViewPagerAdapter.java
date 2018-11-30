package com.rainbell.simple.activity.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    Fragment[] mFragments;
    FragmentManager mFragmentManager;

    public ViewPagerAdapter(FragmentManager fm, Fragment[] fragments) {
        super(fm);
        mFragmentManager = fm;
        mFragments = fragments;
    }

    @Override
    public int getCount() {
        return mFragments.length;
    }

    @Override
    public Fragment getItem(int i) {
        return mFragments[i];
    }


}
