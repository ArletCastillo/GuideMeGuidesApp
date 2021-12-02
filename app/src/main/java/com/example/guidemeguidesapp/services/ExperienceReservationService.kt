package com.example.guidemeguidesapp.services

import android.content.Context
import com.example.guidemeguidesapp.dataModels.ExperienceReservation
import com.example.guidemeguidesapp.dataModels.ExperienceReservationRequest
import com.example.guidemeguidesapp.interfaces.IReservationServiceAPI
import com.example.guidemetravelersapp.helpers.RetrofitInstance
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class ExperienceReservationService(context: Context) {
    private val retrofitInstance = RetrofitInstance.getRetrofit(context)
    private val apiService = retrofitInstance.create(IReservationServiceAPI::class.java)

    suspend fun getGuideReservations(guideFirebaseUserId: String): List<ExperienceReservation> {
        return coroutineScope {
            val getGuideReservationsTask = async { apiService.getGuideReservations("api/Reservations/getGuideReservations/$guideFirebaseUserId").body() }
            getGuideReservationsTask.await()!!
        }
    }

    suspend fun getReservationRequestsForGuide(guideId: String): List<ExperienceReservationRequest> {
        return coroutineScope {
            val getGuideReservationsTask = async { apiService.getReservationRequestsForGuide("api/Reservations/requestForGuide/$guideId").body() }
            getGuideReservationsTask.await()!!
        }
    }

    suspend fun acceptReservationRequest(requestReservationId: String) {
        return  coroutineScope {
            val getReservationTask = async { apiService.acceptReservationRequest("api/Reservations/accept/$requestReservationId").body() }
            getReservationTask.await()!!
        }
    }

    suspend fun rejectReservationRequest(requestReservationId: String) {
        return  coroutineScope {
            val getReservationTask = async { apiService.rejectReservationRequest("api/Reservations/reject/$requestReservationId").body() }
            getReservationTask.await()!!
        }
    }

    suspend fun getReservation(id: String): ExperienceReservation {
        return  coroutineScope {
            val getReservationTask = async { apiService.getReservation("api/Reservations/getReservationById/$id").body() }
            getReservationTask.await()!!
        }
    }

    suspend fun getPastExperiences(guideId: String): List<ExperienceReservation> {
        return coroutineScope {
            val getPastReservationsTask = async { apiService.getPastExperiencesForGuide("api/Reservations/getPastReservationsGuide/$guideId").body() }
            getPastReservationsTask.await()!!
        }
    }

    suspend fun rateExperience(experienceReservation: ExperienceReservation) {
        coroutineScope {
            val rateExperienceTask = async { apiService.rateExperience("api/Reservations/rateReservation", experienceReservation) }
            rateExperienceTask.await()
        }
    }


}