package com.example.materialdesignapp.model

import com.google.gson.annotations.SerializedName

data class SatelliteResponseData(
    @field:SerializedName("url") val url: String?,
    @field:SerializedName("date") val date: String?
)

