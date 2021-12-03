package com.example.guidemeguidesapp.viewModels

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Geocoder
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.guidemeguidesapp.R
import com.example.guidemeguidesapp.dataModels.GuidingOffer
import com.example.guidemeguidesapp.dataModels.ReservationStatus
import com.example.guidemeguidesapp.dataModels.TouristAlert
import com.example.guidemeguidesapp.helpers.pushNotifications.FirebaseNotificationMessagingService
import com.example.guidemeguidesapp.services.AuthenticationService
import com.example.guidemeguidesapp.services.GuideExperienceService
import com.example.guidemeguidesapp.services.TouristAlertService
import com.example.guidemetravelersapp.helpers.models.ApiResponse
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.util.*

class TouristAlertViewModel(application: Application) : AndroidViewModel(application) {
    private val touristAlertService: TouristAlertService = TouristAlertService(application)
    private val profileService: AuthenticationService = AuthenticationService(application)
    private var fusedLocationClient: FusedLocationProviderClient

    var touristAlerts: ApiResponse<List<TouristAlert>> by mutableStateOf(ApiResponse(data = listOf(), inProgress = true))
    var guideOffers: ApiResponse<List<GuidingOffer>> by mutableStateOf(ApiResponse(data = listOf(), inProgress = true))

    var currentCityLocation: String by mutableStateOf("")

    var newGuideOfferStatus: ApiResponse<Boolean> by mutableStateOf(ApiResponse(data = false, inProgress = false))

    init {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)
        fetchTouristAlerts()
    }

    fun fetchTouristAlerts() {
        viewModelScope.launch {
            try {
                if (ActivityCompat.checkSelfPermission(
                        getApplication(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        getApplication(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return@launch
                }
                else {
                    val currentLocation = fusedLocationClient.lastLocation.await()
                    val geocoder = Geocoder(getApplication(), Locale.getDefault())
                    val addresses = geocoder.getFromLocation(currentLocation.latitude, currentLocation.longitude, 1);
                    if (addresses.size > 0) {
                        currentCityLocation = addresses[0].locality
                        val result = profileService.getCurrentFirebaseUserId()
                            ?.let { touristAlertService.getTouristAlerts(currentCityLocation, it) }
                        touristAlerts = ApiResponse(data = result, inProgress = false)
                    }
                }
            }
            catch (e: Exception) {
                touristAlerts = ApiResponse(inProgress = false, hasError = true, errorMessage = e.toString())
                Log.d(TouristAlertViewModel::class.simpleName, "ERROR: ${e.localizedMessage}")
            }
        }
    }

    fun sendGuideOffer(touristAlert: TouristAlert) {
        viewModelScope.launch {
            try {
                newGuideOfferStatus = ApiResponse(false, true)
                val currentGuideId = profileService.getCurrentFirebaseUserId()
                val guideUser = profileService.getUserByFirebaseId(currentGuideId!!)
                val guideOffer = GuidingOffer(
                    touristFirstName = touristAlert.touristFirstName,
                    touristLastName = touristAlert.touristLastName,
                    guideId = currentGuideId,
                    touristId = touristAlert.touristId,
                    touristDestination = touristAlert.touristDestination,
                    touristAlertId = touristAlert.id,
                    reservationStatus = ReservationStatus.PENDING.ordinal,
                    fromDate = touristAlert.fromDate,
                    toDate = touristAlert.toDate
                )
                touristAlertService.sendGuideOffer(guideOffer)
                newGuideOfferStatus = ApiResponse(true, false)
                val instanceId = profileService.getInstanceId(touristAlert.touristId)
                FirebaseNotificationMessagingService.sendNotification(
                    getApplication<Application>().resources.getString(R.string.guide_offer_title_notification),
                    "${guideUser?.firstName} ${getApplication<Application>().resources.getString(R.string.guide_offer_body_notification)} ${guideOffer.touristDestination}",
                    instanceId!!)
            }
            catch (e: Exception) {
                newGuideOfferStatus = ApiResponse(false, false, true, e.toString())
                Log.d(TouristAlertViewModel::class.simpleName, "ERROR: $e")
            }
        }
    }

    fun getGuideOffers() {
        viewModelScope.launch {
            try {
                val currentUserId = profileService.getCurrentFirebaseUserId()
                val result =  touristAlertService.getGuideOffers(currentUserId!!)
                guideOffers = ApiResponse(data = result, inProgress = false)
            }
            catch (e: Exception) {
                guideOffers = ApiResponse(listOf(), false, true, e.toString())
                Log.d(TouristAlertViewModel::class.simpleName, "ERROR: $e")
            }
        }
    }
}