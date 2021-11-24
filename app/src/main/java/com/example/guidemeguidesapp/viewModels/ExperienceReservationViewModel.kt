package com.example.guidemeguidesapp.viewModels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.guidemeguidesapp.dataModels.ExperienceReservation
import com.example.guidemeguidesapp.services.ExperienceReservationService
import kotlinx.coroutines.launch
import java.lang.Exception

class ExperienceReservationViewModel(application: Application) : AndroidViewModel(application) {
    private val experienceReservationService : ExperienceReservationService = ExperienceReservationService(application)
    var guideReservations: List<ExperienceReservation> by mutableStateOf(listOf())
    var reservation: ExperienceReservation = ExperienceReservation()

    fun getGuideReservations(guideId: String) {
        viewModelScope.launch {
            try {
                val result = experienceReservationService.getGuideReservations(guideId = guideId)
                guideReservations = result
            } catch (e: Exception) {
                Log.d(ExperienceReservationViewModel::class.simpleName, "ERROR: ${e.localizedMessage}")
            }
        }
    }

    fun getReservation(id: String, profileViewModel: ProfileViewModel) {
        viewModelScope.launch {
            try {
                val result = experienceReservationService.getReservation(id = id)
                reservation = result
                callProfileViewModelGetUser(result.touristUserId, profileViewModel)
            } catch (e: Exception) {
                Log.d(ExperienceReservationViewModel::class.simpleName, "ERROR: ${e.localizedMessage}")
            }
        }
    }

    fun callProfileViewModelGetUser(userId: String, profileViewModel: ProfileViewModel) {
        profileViewModel.userId = userId
        profileViewModel.getUserById()
    }
}