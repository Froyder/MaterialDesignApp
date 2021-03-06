package com.example.materialdesignapp.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.materialdesignapp.R
import com.example.materialdesignapp.databinding.DateLayoutBinding
import com.google.android.material.chip.Chip
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DateFragment : DialogFragment() {

    private var _binding: DateLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = DateLayoutBinding.inflate(inflater, container, false)
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
                dialog?.cancel()
                //activity?.supportFragmentManager?.popBackStack()
            }
        }
    }

    private fun getDate (date : String): String {
        val cal = Calendar.getInstance()
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

        when (date) {
            getString(R.string.today) -> cal.add(Calendar.DATE, 0)
            getString(R.string.yesterday) -> cal.add(Calendar.DATE, -1)
            getString(R.string.day_before_yesterday) -> cal.add(Calendar.DATE, -2)
        }

        return dateFormat.format(cal.time)
    }
}