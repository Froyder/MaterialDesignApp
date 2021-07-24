package com.example.materialdesignapp.ui.view.BND

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.materialdesignapp.R
import com.example.materialdesignapp.databinding.BottomNavigationLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomNavigationDrawerFragment : BottomSheetDialogFragment() {

    private var _binding: BottomNavigationLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = BottomNavigationLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_one -> activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.main_container, CollapsingToolbarFragment())?.addToBackStack("")
                    ?.commit()

                R.id.navigation_two -> activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.main_container, MotionLayoutFragment())?.addToBackStack("")
                    ?.commit()

                R.id.navigation_three -> activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.main_container, AnimationsFragment())?.addToBackStack("")
                    ?.commit()
            }
            true
        }
    }
}