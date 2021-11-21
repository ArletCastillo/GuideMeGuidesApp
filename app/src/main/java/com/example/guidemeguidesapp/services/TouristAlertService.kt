package com.example.guidemeguidesapp.services

import android.content.Context
import com.example.guidemeguidesapp.dataModels.TouristAlert
import com.example.guidemeguidesapp.interfaces.ITouristAlertServiceApi
import com.example.guidemetravelersapp.helpers.RetrofitInstance
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class TouristAlertService(context: Context) {
    private val retrofitInstance = RetrofitInstance.getRetrofit(context)
    private val apiService = retrofitInstance.create(ITouristAlertServiceApi::class.java)

    suspend fun getTouristAlerts(): List<TouristAlert> {
        return coroutineScope {
            val getTouristAlertTask = async { apiService.getAll("api/TouristAlert").body() }
            getTouristAlertTask.await()!!
        }
    }
}