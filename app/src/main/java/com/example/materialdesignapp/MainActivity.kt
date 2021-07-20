package com.example.materialdesignapp

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.materialdesignapp.ui.view.BND.BottomNavigationDrawerFragment
import com.example.materialdesignapp.ui.view.PoD.DateFragment
import com.example.materialdesignapp.ui.view.fragments.SearchFragment
import com.example.materialdesignapp.ui.view.SettingsFragment
import com.example.materialdesignapp.ui.view.fragments.ViewPagerAdapter
import kotlinx.android.synthetic.main.main_fragment_viewpager.*
import kotlinx.android.synthetic.main.main_fragment_viewpager.app_bar
import kotlinx.android.synthetic.main.main_fragment_viewpager.fab

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        setTheme(sharedPref.getInt("Theme", 1))

        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_fragment_viewpager)

        //main_fragment_viewpager
        view_pager.adapter = ViewPagerAdapter(supportFragmentManager)
        setupTab()

        //main_fragment_bottom_nav
        //setButtons()

        setSupportActionBar(app_bar)
        setFab()

        //val badge = bottom_navigation_view.getOrCreateBadge(R.id.bottom_view_mars)
    }

    private fun setupTab() {
        tab_layout.setupWithViewPager(view_pager)
        tab_layout.getTabAt(0)?.setIcon(R.drawable.ic_earth)
        tab_layout.getTabAt(1)?.setIcon(R.drawable.ic_mars)
        tab_layout.getTabAt(2)?.setIcon(R.drawable.ic_system)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_app_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                this.let {
                    BottomNavigationDrawerFragment().show(it.supportFragmentManager, "tag")
                }
            }
            R.id.app_bar_favorite -> Toast.makeText(this,R.string.under_construction, Toast.LENGTH_SHORT).show()
            R.id.app_bar_settings -> showSettings()
            R.id.app_bar_date -> showDate()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDate() {
        val searchFragment = DateFragment()
        val manager = supportFragmentManager
        searchFragment.show(manager, "dateDialog")
    }

    private fun showSearch(){
        val searchFragment = SearchFragment()
        val manager = supportFragmentManager
        searchFragment.show(manager, "searchDialog")
    }

    private fun showSettings() {
        val settingsFragment = SettingsFragment()
        val manager = supportFragmentManager
        settingsFragment.show(manager, "settingsDialog")
    }

    private fun setFab () {
        fab.setOnClickListener {
            showSearch()
        }
    }

//        private fun setButtons () {
//        bottom_navigation_view.setOnNavigationItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.bottom_view_earth -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.activity_api_bottom_container, PoDFragment())
//                        .commitAllowingStateLoss()
//                    true
//                }
//                R.id.bottom_view_mars -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.activity_api_bottom_container, MarsFragment())
//                        .commitAllowingStateLoss()
//                    true
//                }
//                R.id.bottom_view_weather -> {
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.activity_api_bottom_container, SatelliteFragment())
//                        .commitAllowingStateLoss()
//                    true
//                }
//                else -> false
//            }
//        }
//
//        bottom_navigation_view.selectedItemId = R.id.bottom_view_earth
//
//        bottom_navigation_view.setOnNavigationItemReselectedListener { item ->
//            when (item.itemId) {
//                R.id.bottom_view_earth -> {
//                    //Item tapped
//                }
//                R.id.bottom_view_mars -> {
//                    //Item tapped
//                }
//                R.id.bottom_view_weather -> {
//                    //Item tapped
//                }
//            }
//        }
//    }
}