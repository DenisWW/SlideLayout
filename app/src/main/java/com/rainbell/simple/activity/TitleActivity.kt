package com.rainbell.simple.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import com.rainbell.simple.R
import com.rainbell.simple.activity.adapter.DomeFragment
import com.rainbell.simple.activity.adapter.ViewPagerAdapterKotlin
import com.rainbell.www.slidelayout.customize.CusRelativeManager
import kotlinx.android.synthetic.main.activity_title.*

class TitleActivity : AppCompatActivity() {
    var fragments: Array<Fragment>? = null
    var view_Pager: ViewPager? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title)
        view_Pager = findViewById(R.id.view_Pager)
        fragments = arrayOf(DomeFragment(), DomeFragment(), DomeFragment())
        view_Pager!!.adapter = ViewPagerAdapterKotlin(supportFragmentManager, fragments!!)
        val cusRelativeManager = CusRelativeManager(CusRelativeManager.Builder(cus_relative, view_Pager))
    }
}
