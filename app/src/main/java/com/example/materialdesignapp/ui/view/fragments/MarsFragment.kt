package com.example.materialdesignapp.ui.view.fragments

import android.os.Bundle
import android.transition.*
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import coil.api.load
import com.example.materialdesignapp.R
import com.example.materialdesignapp.viewmodel.MarsPodData
import com.example.materialdesignapp.viewmodel.MarsPodViewModel
import kotlinx.android.synthetic.main.fragment_mars.*
import kotlinx.android.synthetic.main.fragment_mars.constraint_container
import kotlinx.android.synthetic.main.fragment_satellite.*
import kotlinx.android.synthetic.main.main_fragment.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MarsFragment : Fragment() {

    private var isExpanded = false
    private var textIsVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mars, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mars_rover.setOnClickListener {
            TransitionManager.beginDelayedTransition(mars_linear, Slide(Gravity.START))
            textIsVisible = !textIsVisible
            mars_rover.text = if (!textIsVisible) getString(R.string.push_me) else getString(R.string.picture_by_curiosity)
            mars_earth_date.visibility = if (textIsVisible) View.VISIBLE else View.GONE
            mars_sol.visibility = if (textIsVisible) View.VISIBLE else View.GONE
            setImage()
        }

    }

    private fun setImage() {
        isExpanded = !isExpanded
        TransitionManager.beginDelayedTransition(
            constraint_container, TransitionSet()
                .addTransition(ChangeBounds())
                .addTransition(ChangeImageTransform())
        )

        val params: ViewGroup.LayoutParams = mars_image.layoutParams
        params.height =
            if (isExpanded) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT
        mars_image.layoutParams = params
        mars_image.scaleType =
            if (isExpanded) ImageView.ScaleType.CENTER else ImageView.ScaleType.FIT_XY
    }

    private val viewModel: MarsPodViewModel by lazy {
        ViewModelProviders.of(this).get(MarsPodViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val dateOfPicture = getDate()
        viewModel.getData(dateOfPicture).observe(viewLifecycleOwner, { renderData(it) })
    }

    private fun getDate (date : Int = -1): String {
        val cal = Calendar.getInstance()
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        cal.add(Calendar.DATE, date)
        return dateFormat.format(cal.time)
    }

    private fun renderData(data: MarsPodData) {
        when (data) {
            is MarsPodData.Success -> {
                val serverResponseData = data.serverResponseData

                if (!data.serverResponseData.photos.isNullOrEmpty()){
                    val url = serverResponseData.photos[0].img_src

                    if (url.isNullOrEmpty()) {
                        Toast.makeText(context, "1", Toast.LENGTH_LONG).show()
                    }
                    else {
                        showSuccess(url, data)
                    }
                } else {
                    val dateOfPicture = getDate(-2)
                    viewModel.getData(dateOfPicture).observe(viewLifecycleOwner, { renderData(it) })
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

        mars_sol.text = "Sol: ${serverData.serverResponseData.photos[0].sol}"
        mars_earth_date.text = "Earth date: " + serverData.serverResponseData.photos[0].earth_date

    }
}