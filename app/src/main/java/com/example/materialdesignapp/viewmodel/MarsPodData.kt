package com.example.materialdesignapp.viewmodel

import com.example.materialdesignapp.model.MarsPODS
import com.example.materialdesignapp.model.MarsPODServerResponseData
import com.example.materialdesignapp.model.PODServerResponseData

sealed class MarsPodData {
    data class Success(val serverResponseData: MarsPODS) : MarsPodData()
    data class Error(val error: Throwable) : MarsPodData()
    data class Loading(val progress: Int?) : MarsPodData()
}
