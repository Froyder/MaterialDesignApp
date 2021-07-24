package com.example.materialdesignapp.ui.view.fragments

import android.graphics.Rect
import android.os.Bundle
import android.transition.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.materialdesignapp.BuildConfig
import com.example.materialdesignapp.R
import com.example.materialdesignapp.model.SatelliteResponseData
import com.example.materialdesignapp.viewmodel.PODRetrofitImpl
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.fragment_satellite.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SatelliteFragment : Fragment() {

    private var isExpanded = false

    private val retrofitImpl: PODRetrofitImpl = PODRetrofitImpl()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Toast.makeText(context,R.string.loading,Toast.LENGTH_SHORT).show()
        return inflater.inflate(R.layout.fragment_satellite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chipGroup.setOnCheckedChangeListener { chipGroup, position ->
            chipGroup.findViewById<Chip>(position)?.let {
                when (it.text) {
                    "Moscow" -> getSatelliteView("55.755826", "37.617299900000035")
                    "Omsk" -> getSatelliteView("54.99", "73.37")
                    "Custom" -> Toast.makeText(context,R.string.loading,Toast.LENGTH_SHORT).show()
                }
            }
        }

        recycler_view.adapter = Adapter()
    }

    private fun getSatelliteView(lat: String, lon: String) {
        val apiKey = BuildConfig.NASA_API_KEY
        val dim = "0.34"
            retrofitImpl.getSatelliteRetrofitImpl().getWeather(lat, lon, dim, apiKey)
                .enqueue(object : Callback<SatelliteResponseData> {
                    override fun onResponse(call: Call<SatelliteResponseData>, response: Response<SatelliteResponseData>) {
                        val weatherData = (response.body())
                        if (weatherData != null) {
                            satellite_place.text = getString(R.string.current_pic)
                            satellite_date.text = "Date: " + weatherData.date

                            Toast.makeText(context,R.string.loading,Toast.LENGTH_SHORT).show()

                           satellite_view.load(weatherData.url) {
                                lifecycle(viewLifecycleOwner)
                                error(R.drawable.ic_no_photo_vector)
                                placeholder(R.drawable.ic_no_photo_vector)
                            }
                        }
                    }

                    override fun onFailure(call: Call<SatelliteResponseData>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
            explode()
        }

    private fun explode() {
        val viewRect = Rect()
        val explode = Explode()
        explode.epicenterCallback = object : Transition.EpicenterCallback() {
            override fun onGetEpicenter(transition: Transition): Rect {
                return viewRect
            }
        }
        explode.duration = 3000
        TransitionManager.beginDelayedTransition(recycler_view, explode)
        recycler_view.adapter = null
    }

    inner class Adapter : RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.explode_item,
                    parent,
                    false
                ) as View
            )
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        }

        override fun getItemCount(): Int {
            return 16
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}