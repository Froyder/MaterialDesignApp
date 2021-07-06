package com.example.materialdesignapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.materialdesignapp.databinding.ChipsLayoutBinding
import com.google.android.material.chip.Chip
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ChipsFragment : DialogFragment() {

    private var _binding: ChipsLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = ChipsLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.chipGroup.setOnCheckedChangeListener { chipGroup, position ->
            chipGroup.findViewById<Chip>(position)?.let {
                Toast.makeText(context, "Date of picture: ${it.text}", Toast.LENGTH_LONG).show()
                val dateOfPicture = getDate(it.text.toString())
                setFragmentResult(
                        "DATE_REQUEST",
                        bundleOf("date" to dateOfPicture)
                )
                activity?.supportFragmentManager?.popBackStack()
            }
        }
    }

    fun getDate (date : String): String {
        val cal = Calendar.getInstance()
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.UK)

        when (date) {
            "Today" -> cal.add(Calendar.DATE, 0)
            "Yesterday" -> cal.add(Calendar.DATE, -1)
            "Day before yesterday" -> cal.add(Calendar.DATE, -2)
        }

        return dateFormat.format(cal.time)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}