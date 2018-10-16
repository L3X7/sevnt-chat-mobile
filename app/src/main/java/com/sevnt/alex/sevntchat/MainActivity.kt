package com.sevnt.alex.sevntchat

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.sevnt.alex.sevntchat.activities.LoginActivity
import com.sevnt.alex.sevntchat.activities.SearchContactActivity
import com.sevnt.alex.sevntchat.adapters.TabViewPageAdapter
import com.sevnt.alex.sevntchat.fragments.ChatListFragment
import com.sevnt.alex.sevntchat.fragments.ContactListFragment
import com.sevnt.alex.sevntchat.helpers.UserDataBaseHelper
import com.sevnt.alex.sevntchat.models.UserDBModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var mViewPager: ViewPager
    private lateinit var mTabLayout: TabLayout
    private var userDBModel: UserDBModel? = null
    private lateinit var fActionBarContact: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fActionBarContact = findViewById(R.id.fActionBtnAddContact)
        fActionBarContact.setOnClickListener {
            val intent = Intent(this, SearchContactActivity::class.java)
            startActivity(intent)
        }

        val dbHandler = UserDataBaseHelper(this)
        userDBModel = dbHandler.findFirstUser()
        if (userDBModel == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            this.finish()

        }

        setSupportActionBar(toolbar_tab)
        mViewPager = findViewById(R.id.mViewPager)
        mTabLayout = findViewById(R.id.tab_layout_tab)

        setupViewPager(mViewPager)
        mTabLayout.setupWithViewPager(mViewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val idMenu = item?.itemId
        if (idMenu == R.id.action_logout) {
            val dbHandler = UserDataBaseHelper(this)
            dbHandler.deleteUSer(userDBModel?.idUser as String)
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            this.finish()

        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = TabViewPageAdapter(supportFragmentManager)
        adapter.addFragment(ChatListFragment(), "Chats")
        adapter.addFragment(ContactListFragment(), "Contacts")
        viewPager.adapter = adapter

    }
}
