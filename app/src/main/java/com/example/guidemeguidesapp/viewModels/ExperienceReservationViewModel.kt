package com.example.guidemeguidesapp.viewModels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.guidemeguidesapp.R
import com.example.guidemeguidesapp.dataModels.ExperienceReservation
import com.example.guidemeguidesapp.dataModels.ExperienceReservationRequest
import com.example.guidemeguidesapp.helpers.pushNotifications.FirebaseNotificationMessagingService
import com.example.guidemeguidesapp.services.AuthenticationService
import com.example.guidemeguidesapp.services.ExperienceReservationService
import com.example.guidemetravelersapp.helpers.models.ApiResponse
import kotlinx.coroutines.launch
import java.lang.Exception

class ExperienceReservationViewModel(application: Application) : AndroidViewModel(application) {
    private val experienceReservationService : ExperienceReservationService = ExperienceReservationService(application)
    private val profileService: AuthenticationService = AuthenticationService(application)

    var guideReservations: ApiResponse<List<ExperienceReservation>> by mutableStateOf(ApiResponse(data = emptyList(), inProgress = true))
    var reservation: ExperienceReservation = ExperienceReservation()

    var acceptReservationRequest: ApiResponse<Boolean> by mutableStateOf(ApiResponse(data = false, inProgress = false))
    var rejectReservationRequest: ApiResponse<Boolean> by mutableStateOf(ApiResponse(data = false, inProgress = false))

    var guideReservationRequests: ApiResponse<List<ExperienceReservationRequest>> by mutableStateOf(ApiResponse(data = emptyList(), inProgress = true))
    var pastExperienceReservations: ApiResponse<List<ExperienceReservation>> by mutableStateOf(ApiResponse(data = emptyList(), inProgress = true))

    var rateReservationRequestStatus: ApiResponse<Boolean> by mutableStateOf(ApiResponse(data = false, inProgress = false))


    fun getGuideReservations(guideFirebaseUserId: String) {
        viewModelScope.launch {
            try {
                val result = experienceReservationService.getGuideReservations(guideFirebaseUserId = guideFirebaseUserId)
                guideReservations = ApiResponse(data = result, inProgress = false)
            } catch (e: Exception) {
                guideReservations = ApiResponse(inProgress = false, hasError = true, errorMessage = "")
                Log.d(ExperienceReservationViewModel::class.simpleName, "ERROR: ${e.localizedMessage}")
            }
        }
    }

    fun getReservation(id: String, profileViewModel: ProfileViewModel) {
        viewModelScope.launch {
            try {
                val result = experienceReservationService.getReservation(id = id)
                reservation = result
                // touristUserId is the FirebaseUserId
                callProfileViewModelGetUser(result.touristUserId, profileViewModel)
            } catch (e: Exception) {
                Log.d(ExperienceReservationViewModel::class.simpleName, "ERROR: ${e.localizedMessage}")
            }
        }
    }

    fun getReservationRequestsForGuide() {
        viewModelScope.launch {
            try {
                val guideId = profileService.getCurrentFirebaseUserId()
                val result = guideId?.let { experienceReservationService.getReservationRequestsForGuide(guideId = it) }
                guideReservationRequests = ApiResponse(data = result, inProgress = false)
            } catch (e: Exception) {
                guideReservationRequests = ApiResponse(inProgress = false, hasError = true, errorMessage = "")
                Log.d(ExperienceReservationViewModel::class.simpleName, "ERROR: $e")
            }
        }
    }

    fun acceptReservationRequest(reservationRequest: ExperienceReservationRequest) {
        viewModelScope.launch {
            try {
                acceptReservationRequest = ApiResponse(false, true)
                experienceReservationService.acceptReservationRequest(reservationRequest.id)
                acceptReservationRequest = ApiResponse(true, false)
                getReservationRequestsForGuide()
                val instanceId = profileService.getInstanceId(reservationRequest.touristUserId)
                FirebaseNotificationMessagingService.sendNotification(
                    getApplication<Application>().resources.getString(R.string.guide_accepted_request_title_notification),
                    "${reservationRequest.guideFirstName} ${getApplication<Application>().resources.getString(R.string.guide_accepted_request_body_notification)}",
                    instanceId!!)
            }
            catch (e: Exception) {
                acceptReservationRequest = ApiResponse(true, false)
                Log.d(ExperienceReservationViewModel::class.simpleName, "ERROR: $e")
            }
        }
    }

    fun rejectReservationRequest(reservationRequest: ExperienceReservationRequest) {
        viewModelScope.launch {
            try {
                rejectReservationRequest = ApiResponse(false, true)
                experienceReservationService.rejectReservationRequest(reservationRequest.id)
                rejectReservationRequest = ApiResponse(true, false)
                getReservationRequestsForGuide()
                val instanceId = profileService.getInstanceId(reservationRequest.touristUserId)
                FirebaseNotificationMessagingService.sendNotification(
                    getApplication<Application>().resources.getString(R.string.guide_rejected_request_title_notification),
                    "${reservationRequest.guideFirstName} ${getApplication<Application>().resources.getString(R.string.guide_rejected_request_body_notification)}",
                    instanceId!!)
            }
            catch (e: Exception) {
                rejectReservationRequest = ApiResponse(true, false)
                Log.d(ExperienceReservationViewModel::class.simpleName, "ERROR: $e")
            }
        }
    }

    private fun callProfileViewModelGetUser(touristFirebaseUserId: String, profileViewModel: ProfileViewModel) {
        profileViewModel.firebaseUserId = touristFirebaseUserId
        profileViewModel.getUserByFirebaseId()
    }

    fun getPastExperiences() {
        viewModelScope.launch {
            val userId = profileService.getCurrentFirebaseUserId()
            try {
                val result = userId?.let { experienceReservationService.getPastExperiences(it) }
                pastExperienceReservations = ApiResponse(data = result, inProgress = false)
            }
            catch (e: Exception) {
                Log.d(ExperienceReservationViewModel::class.simpleName, "ERROR: ${e.localizedMessage}")
                pastExperienceReservations = ApiResponse(inProgress = false, hasError = true, errorMessage = "")
            }
        }
    }

    fun rateExperience(experienceReservation: ExperienceReservation) {
        viewModelScope.launch {
            try {
                rateReservationRequestStatus = ApiResponse(false, true)
                experienceReservationService.rateTourist(experienceReservation)
                rateReservationRequestStatus = ApiResponse(true, false)
                pastExperienceReservations = ApiResponse(data = emptyList(), inProgress = true)
                getPastExperiences()
            }
            catch (e: Exception) {
                rateReservationRequestStatus = ApiResponse(true, false)
                Log.d(ExperienceReservationViewModel::class.simpleName, "ERROR: $e")
            }
        }
    }


}