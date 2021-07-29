package com.example.materialdesignapp.ui.view.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.recreate
import androidx.fragment.app.DialogFragment
import com.example.materialdesignapp.R
import com.example.materialdesignapp.databinding.SettingsFragmentBinding
import com.google.android.material.chip.Chip
import java.util.*

const val Main = R.style.Main
const val Alternative = R.style.Alternative

class SettingsFragment : DialogFragment() {

    private var _binding: SettingsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.settingsChipGroup.setOnCheckedChangeListener { chipGroup, position ->
            chipGroup.findViewById<Chip>(position)?.let {
                Toast.makeText(context, "${it.text} theme applied", Toast.LENGTH_LONG).show()
                when (it.text.toString()) {
                    "Main" -> changeTheme(Main)
                    "Alternative" -> changeTheme(Alternative)
                }
            }
        }
    }

    private fun changeTheme (theme: Int) {

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        if (sharedPref != null) {
            dialog?.cancel()
            with (sharedPref.edit()) {
                putInt("Theme", theme)
                apply()
                activity?.recreate()
            }
        }
    }

}