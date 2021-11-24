package com.example.guidemeguidesapp.services

import android.content.Context
import com.example.guidemeguidesapp.dataModels.GuidingOffer
import com.example.guidemeguidesapp.dataModels.TouristAlert
import com.example.guidemeguidesapp.interfaces.ITouristAlertServiceApi
import com.example.guidemetravelersapp.helpers.RetrofitInstance
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class TouristAlertService(context: Context) {
    private val retrofitInstance = RetrofitInstance.getRetrofit(context)
    private val apiService = retrofitInstance.create(ITouristAlertServiceApi::class.java)

    suspend fun getTouristAlerts(locationCity: String, currentUserId: String): List<TouristAlert> {
        return coroutineScope {
            val getTouristAlertTask = async { apiService.getAll("api/TouristAlert/getAll/$locationCity/$currentUserId").body() }
            getTouristAlertTask.await()!!
        }
    }

    suspend fun sendGuideOffer(guidingOffer: GuidingOffer) {
        return coroutineScope {
            val sendGuideOfferTask = async { apiService.sendGuideOffer("api/TouristAlert/guideOffers", guidingOffer).body() }
            sendGuideOfferTask.await()!!
        }
    }
}