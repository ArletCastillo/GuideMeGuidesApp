package com.example.guidemeguidesapp.interfaces

import com.example.guidemeguidesapp.dataModels.TouristAlert
import com.example.guidemeguidesapp.dataModels.User
import retrofit2.Response
import retrofit2.http.*

interface ITouristAlertServiceApi {
    @POST
    suspend fun post(@Url url:String, @Body body: User) : Response<Unit>

    @GET
    suspend fun getAll(@Url url:String) : Response<List<TouristAlert>>
}