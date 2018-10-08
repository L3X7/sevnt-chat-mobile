package com.sevnt.alex.sevntchat

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.sevnt.alex.sevntchat.adapters.TabViewPageAdapter
import com.sevnt.alex.sevntchat.fragments.ChatFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var mViewPager: ViewPager
    private lateinit var mTabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar_tab)
        mViewPager = findViewById(R.id.mViewPager)
        mTabLayout = findViewById(R.id.tab_layout_tab)

        setupViewPager(mViewPager)
        mTabLayout.setupWithViewPager(mViewPager)
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = TabViewPageAdapter(supportFragmentManager)
        adapter.addFragment(ChatFragment(), "Chats")
        viewPager.adapter = adapter

    }
}
