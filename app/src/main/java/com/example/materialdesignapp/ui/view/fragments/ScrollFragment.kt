package com.example.materialdesignapp.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.materialdesignapp.R
import com.example.materialdesignapp.databinding.MainScrollLayoutBinding

class ScrollFragment : Fragment() {

    private var _binding: MainScrollLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = MainScrollLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = ViewPagerAdapter (parentFragmentManager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        binding.tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_earth)
        binding.tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_mars)
        binding.tabLayout.getTabAt(2)?.setIcon(R.drawable.ic_system)

//        setCustomTabs()
    }

//    private fun setCustomTabs() {
//        val layoutInflater = LayoutInflater.from(context)
//        binding.tabLayout.getTabAt(0)?.customView =
//            layoutInflater.inflate(R.layout.activity_api_custom_tab_earth, null)
//        binding.tabLayout.getTabAt(1)?.customView =
//            layoutInflater.inflate(R.layout.activity_api_custom_tab_mars, null)
//        binding.tabLayout.getTabAt(2)?.customView =
//            layoutInflater.inflate(R.layout.activity_api_custom_tab_weather, null)
//    }

}