package com.example.guidemeguidesapp.viewModels

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.guidemeguidesapp.dataModels.Address
import com.example.guidemeguidesapp.dataModels.User
import com.example.guidemeguidesapp.services.AuthenticationService
import com.example.guidemetravelersapp.helpers.models.ApiResponse
import com.example.guidemetravelersapp.helpers.models.Coordinate
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.launch
import java.lang.Exception

class ProfileViewModel(application: Application): AndroidViewModel(application) {
    private val profileService: AuthenticationService = AuthenticationService(application)
    var profileData: ApiResponse<User> by mutableStateOf(ApiResponse(data = User(), inProgress = true))
    var userId: String by mutableStateOf("")
    var userData: ApiResponse<User> by mutableStateOf(ApiResponse(data = User(), inProgress = true))
    var editableUser: User by mutableStateOf(User())
    var updateProfileResult: ApiResponse<Boolean> by mutableStateOf(ApiResponse(data = false, inProgress = false))
    var locationSearchValue: String by mutableStateOf("")
    var predictions: MutableList<AutocompletePrediction> = mutableListOf()
    var placesClient: PlacesClient = Places.createClient(application)

    init {
        getCurrentUserProfile()
    }

    private fun getCurrentUserProfile() {
        viewModelScope.launch {
            try {
                val currentUser = profileService.getCurrentFirebaseUser()
                val result = profileService.getUserByFirebaseId(currentUser?.uid!!)
                profileData = ApiResponse(data = result, inProgress = false)
                editableUser = result!!
            } catch (e: Exception) {
                profileData = ApiResponse(inProgress = false, hasError = true, errorMessage = "")
            }
        }
    }

    fun signOutUser() {
        profileService.signOut()
    }

    /**
     * Get the user data by object id.
     */
    fun getUserById() {
        viewModelScope.launch {
            try {
                if(userId.isNotEmpty()) {
                    val result = profileService.getUserById(id = userId)
                    userData = ApiResponse(data = result, inProgress = false)
                }
            } catch (e: Exception) {
                userData = ApiResponse(inProgress = false, hasError = true, errorMessage = "ERROR: ${e.localizedMessage}")
            }
        }
    }

    /**
     * Calculates the average user rating given all the reviews for the
     * specified user.
     * @return Float with the average rating
     */
    fun calculateUserRating(): Float {
        var totalRating = 0.0f
        if (profileData.data!!.reviews.isEmpty())
            return totalRating
        else {
            profileData.data!!.reviews.forEach { review ->
                totalRating += review.ratingValue
            }
        }
        return totalRating / profileData.data!!.reviews.size
    }

    fun saveProfileChange(fileUri: Uri?) {
        viewModelScope.launch {
            try {
                updateProfileResult = ApiResponse(false, true)
                val result = profileService.updateUser(editableUser)
                var photo_result = false
                if(fileUri != null) {
                    photo_result = profileService.updateUserProfilePhoto(editableUser, fileUri)
                }
                val updateSuccessful = result && photo_result
                profileData = ApiResponse(data = editableUser, inProgress = false)
                updateProfileResult = ApiResponse(data = updateSuccessful, inProgress = false)
            } catch (e: Exception) {
//                Log.d(ProfileViewModel::class.simpleName, "ERROR: ${e.localizedMessage}")
                profileData = ApiResponse(inProgress = false, hasError = true, errorMessage = "")
            }
        }
    }

    fun onQueryChanged(inputText: String) {
        locationSearchValue = inputText
        val request =
            FindAutocompletePredictionsRequest.builder()
                .setTypeFilter(TypeFilter.REGIONS)
                .setQuery(inputText)
                .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                predictions = mutableListOf()
                for (prediction in response.autocompletePredictions) {
                    predictions.add(prediction)
                }
            }
    }

    fun onPlaceItemSelected(placeItem: AutocompletePrediction) {
        locationSearchValue = placeItem.getPrimaryText(null).toString()
        predictions = mutableListOf()
        placesClient.fetchPlace(
            FetchPlaceRequest.newInstance(placeItem.placeId, mutableListOf(Place.Field.LAT_LNG))
        ).addOnSuccessListener {
            editableUser.address = Address(
                city = placeItem.getPrimaryText(null).toString(),
                country = placeItem.getSecondaryText(null).toString(),
                coordinates = Coordinate(
                    latitude = it.place.latLng!!.latitude,
                    longitude = it.place.latLng!!.longitude,
                )
            )
        }
    }
}