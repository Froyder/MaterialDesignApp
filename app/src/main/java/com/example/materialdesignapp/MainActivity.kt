package com.example.materialdesignapp

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.materialdesignapp.ui.view.BND.BottomNavigationDrawerFragment
import com.example.materialdesignapp.ui.view.fragments.DateFragment
import com.example.materialdesignapp.ui.view.ZoomOutPageTransformer
import com.example.materialdesignapp.ui.view.fragments.NotesFragment
import com.example.materialdesignapp.ui.view.fragments.SettingsFragment
import com.example.materialdesignapp.ui.view.fragments.ViewPagerAdapter
import kotlinx.android.synthetic.main.main_fragment_viewpager.*
import kotlinx.android.synthetic.main.main_fragment_viewpager.app_bar
import kotlinx.android.synthetic.main.main_fragment_viewpager.fab
import kotlinx.android.synthetic.main.search_layout.*

class MainActivity : AppCompatActivity() {

    private var isFabExpanded = false

    override fun onCreate(savedInstanceState: Bundle?) {

        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        setTheme(sharedPref.getInt("Theme", 1))

        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_fragment_viewpager)

        //main_fragment_viewpager
        view_pager.adapter = ViewPagerAdapter(supportFragmentManager)
        view_pager.setPageTransformer(true, ZoomOutPageTransformer())
        setupTab()

        //main_fragment_bottom_nav
        //setButtons()

        setSupportActionBar(app_bar)
        setFab()

        frame_background.apply {
            alpha = 0f
        }

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
            R.id.app_bar_notes -> showNotes()
            R.id.app_bar_settings -> showSettings()
            R.id.app_bar_date -> showDate()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun showNotes() {
//            supportFragmentManager.beginTransaction().replace(R.id.main_background, NotesFragment())
//                .addToBackStack("notes_fragment")
//                .commitAllowingStateLoss()

        startActivity(
            Intent(
                this,
                NotesActivity::class.java
            )
        )
    }

    private fun showDate() {
        val searchFragment = DateFragment()
        val manager = supportFragmentManager
        searchFragment.show(manager, "dateDialog")
    }

//    private fun showSearch(){
//        val searchFragment = SearchFragment()
//        val manager = supportFragmentManager
//        searchFragment.show(manager, "searchDialog")
//    }

    private fun showSettings() {
        val settingsFragment = SettingsFragment()
        val manager = supportFragmentManager
        settingsFragment.show(manager, "settingsDialog")
    }

    private fun setFab () {
        input_layout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${input_edit_text.text.toString()}")
            })
        }

        fab.setOnClickListener {
            if (isFabExpanded) {
                collapseFab()
            } else {
                expandFAB()
            }
        }
    }

    private fun expandFAB() {
        isFabExpanded = true
        ObjectAnimator.ofFloat(search_popup, "translationY", 1400f).start()
        fab.animate()
            .scaleXBy(0.2f)
            .scaleYBy(0.2f)
            .duration = 300

        frame_background.animate()
            .alpha(0.8f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    frame_background.isClickable = true
                }
            })

        frame_background.setOnClickListener {
            collapseFab ()
        }
    }

    private fun collapseFab () {
        isFabExpanded = false
        ObjectAnimator.ofFloat(search_popup, "translationY", -1400f).start()

        fab.animate()
            .scaleXBy(-0.2f)
            .scaleYBy(-0.2f)
            .duration = 300

        frame_background.animate()
            .alpha(0.0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    frame_background.isClickable = false
                }
            })

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