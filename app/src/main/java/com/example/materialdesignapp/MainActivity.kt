package com.example.materialdesignapp

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.materialdesignapp.ui.view.BottomNavigationDrawerFragment
import com.example.materialdesignapp.ui.view.PoD.DateFragment
import com.example.materialdesignapp.ui.view.SearchFragment
import com.example.materialdesignapp.ui.view.SettingsFragment
import com.example.materialdesignapp.ui.view.bottom_navigation_view.PoDFragment
import com.example.materialdesignapp.ui.view.bottom_navigation_view.MarsFragment
import com.example.materialdesignapp.ui.view.bottom_navigation_view.SatelliteFragment
import com.google.android.material.bottomappbar.BottomAppBar
import kotlinx.android.synthetic.main.main_navigation_view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        setTheme(sharedPref.getInt("Theme", 1))

        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_navigation_view)

        setSupportActionBar(app_bar)
        setFab()
        //val badge = bottom_navigation_view.getOrCreateBadge(R.id.bottom_view_mars)
        setButtons()
    }

    private fun setButtons () {
        bottom_navigation_view.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_view_earth -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.activity_api_bottom_container, PoDFragment())
                        .commitAllowingStateLoss()
                    true
                }
                R.id.bottom_view_mars -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.activity_api_bottom_container, MarsFragment())
                        .commitAllowingStateLoss()
                    true
                }
                R.id.bottom_view_weather -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.activity_api_bottom_container, SatelliteFragment())
                        .commitAllowingStateLoss()
                    true
                }
                else -> false
            }
        }

        bottom_navigation_view.selectedItemId = R.id.bottom_view_earth

        bottom_navigation_view.setOnNavigationItemReselectedListener { item ->
            when (item.itemId) {
                R.id.bottom_view_earth -> {
                    //Item tapped
                }
                R.id.bottom_view_mars -> {
                    //Item tapped
                }
                R.id.bottom_view_weather -> {
                    //Item tapped
                }
            }
        }
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
            R.id.app_bar_favorite -> Toast.makeText(this,"This function is under construction", Toast.LENGTH_SHORT).show()
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
}