package com.example.guidemeguidesapp.viewModels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.guidemeguidesapp.dataModels.GuideExperience
import com.example.guidemeguidesapp.services.AuthenticationService
import com.example.guidemeguidesapp.services.GuideExperienceService
import com.example.guidemetravelersapp.helpers.models.ApiResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception

class GuideExperienceViewModel(application: Application) : AndroidViewModel(application) {
    private val guideExperienceService: GuideExperienceService = GuideExperienceService(application)
    private val profileService: AuthenticationService = AuthenticationService(application)

    var guideExperience: ApiResponse<GuideExperience> by mutableStateOf(ApiResponse(data = GuideExperience(), inProgress = true))
    var updateGuideExperienceStatus: ApiResponse<Boolean> by mutableStateOf(ApiResponse(data = false, inProgress = false))
    var editableGuideExperience: GuideExperience by mutableStateOf(GuideExperience())

    init {
        getExperience()
    }

    fun updateExperience(tags: List<String>) {
        viewModelScope.launch {
            try {
                updateGuideExperienceStatus = ApiResponse(data = false, inProgress = true)
                val currentGuideId = profileService.getCurrentFirebaseUserId()
                editableGuideExperience.guideFirebaseId = currentGuideId!!
                editableGuideExperience.experienceTags = tags.toMutableList()
                guideExperienceService.saveGuideExperience(editableGuideExperience)
                updateGuideExperienceStatus = ApiResponse(data = true, inProgress = false)
                delay(2000)
                updateGuideExperienceStatus = ApiResponse(data = false, inProgress = false)
            }
            catch (e: Exception) {
                updateGuideExperienceStatus = ApiResponse(data = false, inProgress = false, hasError = true)
                Log.d(GuideExperienceViewModel::class.simpleName, "ERROR: $e")
            }
        }
    }

    private fun getExperience() {
        viewModelScope.launch {
            try {
                val currentGuideId = profileService.getCurrentFirebaseUserId()
                if(!currentGuideId.isNullOrEmpty()) {
                    val result = guideExperienceService.getExperience(guideId = currentGuideId)
                    guideExperience = ApiResponse(data = result, inProgress = false)
                    editableGuideExperience = result
                }
            }
            catch (e: Exception) {
                Log.d(GuideExperienceViewModel::class.simpleName, "ERROR: ${e.localizedMessage}")
            }
        }
    }
}