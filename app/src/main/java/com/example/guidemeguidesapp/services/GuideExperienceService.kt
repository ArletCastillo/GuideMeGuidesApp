package com.example.guidemeguidesapp.services

import android.content.Context
import com.example.guidemeguidesapp.dataModels.GuideExperience
import com.example.guidemeguidesapp.interfaces.IGuideExperienceServiceApi
import com.example.guidemetravelersapp.helpers.RetrofitInstance
import com.example.guidemetravelersapp.helpers.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class GuideExperienceService(context: Context) {
    private var auth: FirebaseAuth = Firebase.auth
    private val TAG = GuideExperienceService::class.simpleName
    private val retrofitInstance = RetrofitInstance.getRetrofit(context)
    private val apiService = retrofitInstance.create(IGuideExperienceServiceApi::class.java)
    private val sessionManager: SessionManager = SessionManager(context)

    suspend fun getExperience(guideId: String): GuideExperience {
        return coroutineScope {
            val getExperienceTask = async { apiService.get("api/GuideExperience/getGuideExperience/$guideId").body() }
            getExperienceTask.await()!!
        }
    }

    suspend fun getExperiencesByUserId(userId: String): List<GuideExperience> {
        return coroutineScope {
            val getExperiences = async { apiService.getAll("api/GuideExperience/getUserById/$userId").body() }
            getExperiences.await()!!
        }
    }

    suspend fun saveGuideExperience(guideExperience: GuideExperience) {
        return coroutineScope {
            val updateExperienceTask = async {
                if(guideExperience.id.isEmpty())
                    apiService.post("api/GuideExperience", guideExperience).body()
                else
                    apiService.put("api/GuideExperience", guideExperience).body()
            }
            updateExperienceTask.await()!!
        }
    }

}