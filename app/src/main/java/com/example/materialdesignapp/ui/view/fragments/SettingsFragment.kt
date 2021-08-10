package com.example.materialdesignapp.ui.view.fragments

import android.content.Context.MODE_PRIVATE
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.materialdesignapp.R
import com.example.materialdesignapp.databinding.SettingsFragmentBinding
import com.google.android.material.chip.Chip

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

        binding.themeMainChip.typeface = Typeface.createFromAsset(context?.assets, "raleway_bold.ttf")
        binding.themeAlternativeChip.typeface = Typeface.createFromAsset(context?.assets, "insomnia.ttf")

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

        val sharedPref = activity?.getSharedPreferences("THEME", MODE_PRIVATE)
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