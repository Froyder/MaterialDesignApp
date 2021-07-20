package com.example.materialdesignapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class MarsPODS(
    val photos: List<MarsPODServerResponseData>
)

data class MarsPODServerResponseData (
    @field:SerializedName("sol") val sol: String?,
    @field:SerializedName("earth_date") val earth_date: String?,
    @field:SerializedName("img_src") val img_src: String?,
    @field:SerializedName("name") val name: String?
)

// https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?camera=fhaz&api_key=DEMO_KEY&earth_date=2021-6-3

