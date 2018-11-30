package com.rainbell.simple.activity.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import android.view.View

class ViewPagerAdapterKotlin : FragmentPagerAdapter {

    constructor(fragmentManager: FragmentManager, fragments: Array<Fragment>) : super(fragmentManager) {
        this.fragments = fragments
    }

    var fragments: Array<Fragment>? = null
    override fun getItem(p0: Int): Fragment {

        return fragments!![p0]
    }

    override fun getCount(): Int {
        return fragments!!.size
    }
}