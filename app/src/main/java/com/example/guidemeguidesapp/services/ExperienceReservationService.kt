package com.example.guidemeguidesapp.services

import android.content.Context
import com.example.guidemeguidesapp.dataModels.ExperienceReservation
import com.example.guidemeguidesapp.interfaces.IReservationServiceAPI
import com.example.guidemetravelersapp.helpers.RetrofitInstance
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class ExperienceReservationService(context: Context) {
    private val retrofitInstance = RetrofitInstance.getRetrofit(context)
    private val apiService = retrofitInstance.create(IReservationServiceAPI::class.java)

    suspend fun getGuideReservations(guideId: String): List<ExperienceReservation> {
        return coroutineScope {
            val getGuideReservationsTask = async { apiService.getGuideReservations("api/Reservations/getGuideReservations/$guideId").body() }
            getGuideReservationsTask.await()!!
        }
    }
}