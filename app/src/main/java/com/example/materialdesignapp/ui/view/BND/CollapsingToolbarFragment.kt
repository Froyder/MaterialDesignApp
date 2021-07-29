package com.example.materialdesignapp.ui.view.BND

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.materialdesignapp.R
import com.example.materialdesignapp.viewmodel.PODRetrofitImpl
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.fragment_satellite.*

class CollapsingToolbarFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.collapsing_fragment_layout, container, false)
    }
}