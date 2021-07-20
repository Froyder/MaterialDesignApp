package com.example.materialdesignapp.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import coil.api.load
import com.example.materialdesignapp.R
import com.example.materialdesignapp.viewmodel.MarsPodData
import com.example.materialdesignapp.viewmodel.MarsPodViewModel
import kotlinx.android.synthetic.main.fragment_mars.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MarsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mars, container, false)
    }

    private val viewModel: MarsPodViewModel by lazy {
        ViewModelProviders.of(this).get(MarsPodViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val dateOfPicture = getDate()
        viewModel.getData(dateOfPicture).observe(viewLifecycleOwner, { renderData(it) })
    }

    private fun getDate (): String {
        val cal = Calendar.getInstance()
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        cal.add(Calendar.DATE, -2)
        return dateFormat.format(cal.time)
    }

    private fun renderData(data: MarsPodData) {
        when (data) {
            is MarsPodData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.photos[0].img_src

                if (url.isNullOrEmpty()) {
                    Toast.makeText(context, "1", Toast.LENGTH_LONG).show()
                } else {
                    showSuccess(url, data)
                }
            }
            is MarsPodData.Loading -> {
                Toast.makeText(context, "Loading data...", Toast.LENGTH_SHORT).show()
            }
            is MarsPodData.Error -> {
                Toast.makeText(context, "3", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showSuccess(url: String, serverData: MarsPodData.Success) {
        mars_image.load(url) {
            lifecycle(viewLifecycleOwner)
            error(R.drawable.ic_no_photo_vector)
            placeholder(R.drawable.ic_no_photo_vector)
        }

        mars_rover.text = getString(R.string.picture_by_curiosity)
        mars_sol.text = "Sol: ${serverData.serverResponseData.photos[0].sol}"
        mars_earth_date.text = "Earth date: " + serverData.serverResponseData.photos[0].earth_date

    }
}