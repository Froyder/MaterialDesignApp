package com.example.materialdesignapp.ui.view.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

private const val POD_FRAGMENT = 0
private const val MARS_FRAGMENT = 1
private const val SATELLITE_FRAGMENT = 2

class ViewPagerAdapter(fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager) {

    private val fragments = arrayOf(PoDFragment(), MarsFragment(), SatelliteFragment())

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> fragments[POD_FRAGMENT]
            1 -> fragments[MARS_FRAGMENT]
            2 -> fragments[SATELLITE_FRAGMENT]
            else -> fragments[POD_FRAGMENT]
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Picture of the day"
            1 -> "Mars by Curiosity"
            2 -> "Satellite View"
            else -> "Picture of the day"
        }
    }

    override fun getCount(): Int {
        return fragments.size
    }
}