package com.example.guidemeguidesapp.viewModels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.guidemeguidesapp.dataModels.TouristAlert
import com.example.guidemeguidesapp.services.TouristAlertService
import kotlinx.coroutines.launch
import java.lang.Exception

class TouristAlertModel(application: Application) : AndroidViewModel(application) {
    private val touristAlertService: TouristAlertService = TouristAlertService(application)
    var touristAlerts: List<TouristAlert> by mutableStateOf(listOf())

    init {
        fetchTouristAlerts()
    }

    private fun fetchTouristAlerts() {
        viewModelScope.launch {
            try {
                val result = touristAlertService.getTouristAlerts()
                touristAlerts = result
            }
            catch (e: Exception) {
                Log.d(TouristAlertModel::class.simpleName, "ERROR: ${e.localizedMessage}")
            }
        }
    }
}