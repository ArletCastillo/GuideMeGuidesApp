package com.example.guidemeguidesapp.viewModels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.guidemeguidesapp.dataModels.User
import com.example.guidemeguidesapp.services.AuthenticationService
import com.example.guidemetravelersapp.helpers.models.ApiResponse
import kotlinx.coroutines.launch
import java.lang.Exception

class ProfileViewModel(application: Application): AndroidViewModel(application) {
    private val profileService: AuthenticationService = AuthenticationService(application)
    var profileData: ApiResponse<User> by mutableStateOf(ApiResponse(data = User(), inProgress = true))

    init {
        getCurrentUserProfile()
    }

    private fun getCurrentUserProfile() {
        viewModelScope.launch {
            profileData = try {
                val currentUser = profileService.getCurrentFirebaseUser()
                val result = profileService.getUserByFirebaseId(currentUser?.uid!!)
                ApiResponse(data = result, inProgress = false)
            } catch (e: Exception) {
                ApiResponse(inProgress = false, hasError = true, errorMessage = "")
            }
        }
    }

    fun signOutUser() {
        profileService.signOut()
    }
}