package com.sevnt.alex.sevntchat

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.sevnt.alex.sevntchat.adapters.TabViewPageAdapter
import com.sevnt.alex.sevntchat.fragments.ChatListFragment
import com.sevnt.alex.sevntchat.fragments.ContactListFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var mViewPager: ViewPager
    private lateinit var mTabLayout: TabLayout
    private lateinit var userOne: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = intent
        userOne = intent.getStringExtra("idUser")
        setSupportActionBar(toolbar_tab)
        mViewPager = findViewById(R.id.mViewPager)
        mTabLayout = findViewById(R.id.tab_layout_tab)

        setupViewPager(mViewPager)
        mTabLayout.setupWithViewPager(mViewPager)
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = TabViewPageAdapter(supportFragmentManager)
        adapter.addFragment(ChatListFragment(), "Chats")
        adapter.addFragment(ContactListFragment(), "Contacts")
        viewPager.adapter = adapter

    }
}
