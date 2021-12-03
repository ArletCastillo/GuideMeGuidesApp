package com.example.guidemeguidesapp.interfaces

import com.example.guidemeguidesapp.dataModels.User
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface INotificationServer {
    @POST("/fcm/send")
    suspend fun post(@Body body: RequestBody, @Header("Authorization") authHeader: String) : Response<Unit>
}