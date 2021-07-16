package com.example.materialdesignapp.viewmodel

import com.example.materialdesignapp.model.SatelliteResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SatelliteAPI {
    @GET("/planetary/earth/assets?")
    fun getWeather(@Query("lat") lat: String, @Query("lon") lon: String, @Query("dim") dim: String,
                   @Query("api_key") apiKey: String): Call<SatelliteResponseData>
}