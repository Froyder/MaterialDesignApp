package com.example.materialdesignapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.materialdesignapp.BuildConfig
import com.example.materialdesignapp.model.MarsPODS
import com.example.materialdesignapp.model.MarsPODServerResponseData
import com.example.materialdesignapp.model.PODServerResponseData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarsPodViewModel(
    private val liveDataForViewToObserve: MutableLiveData<MarsPodData> = MutableLiveData(),
    private val retrofitImpl: PODRetrofitImpl = PODRetrofitImpl()
) :
    ViewModel() {

    fun getData(dateOfPicture : String): LiveData<MarsPodData> {
        sendServerRequest(dateOfPicture)
        return liveDataForViewToObserve
    }

    private fun sendServerRequest(dateOfPicture : String) {
        liveDataForViewToObserve.value = MarsPodData.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            MarsPodData.Error(Throwable("You need an API key"))
        } else {
            retrofitImpl.getMarsRetrofitImpl().getMarsPOD(dateOfPicture, apiKey).enqueue(object :
                Callback<MarsPODS> {

                override fun onResponse(
                    call: Call<MarsPODS>,
                    response: Response<MarsPODS>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        liveDataForViewToObserve.value =
                            MarsPodData.Success(response.body()!!)
                    } else {
                        val message = response.message()
                        if (message.isNullOrEmpty()) {
                            liveDataForViewToObserve.value =
                                MarsPodData.Error(Throwable("Unidentified error"))
                        } else {
                            liveDataForViewToObserve.value =
                                MarsPodData.Error(Throwable(message))
                        }
                    }
                }

                override fun onFailure(call: Call<MarsPODS>, t: Throwable) {
                    liveDataForViewToObserve.value = MarsPodData.Error(t)
                }
            })
        }
    }
}