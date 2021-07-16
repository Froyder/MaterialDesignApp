package com.example.materialdesignapp.viewmodel

import com.example.materialdesignapp.model.MarsPODS
import com.example.materialdesignapp.model.MarsPODServerResponseData
import com.example.materialdesignapp.model.PODServerResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MarsPodAPI {

    @GET("/mars-photos/api/v1/rovers/curiosity/photos?camera=fhaz")
    fun getMarsPOD(@Query("earth_date") earth_date: String, @Query("api_key") apiKey: String): Call<MarsPODS>
}


// https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?camera=fhaz&api_key=DEMO_KEY&earth_date=2021-6-3