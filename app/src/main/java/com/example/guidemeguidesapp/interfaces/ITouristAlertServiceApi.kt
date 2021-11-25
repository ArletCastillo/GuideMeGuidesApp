package com.example.guidemeguidesapp.interfaces

import com.example.guidemeguidesapp.dataModels.GuidingOffer
import com.example.guidemeguidesapp.dataModels.TouristAlert
import com.example.guidemeguidesapp.dataModels.User
import retrofit2.Response
import retrofit2.http.*

interface ITouristAlertServiceApi {
    @POST
    suspend fun sendGuideOffer(@Url url:String, @Body body: GuidingOffer) : Response<Unit>

    @GET
    suspend fun getAll(@Url url:String) : Response<List<TouristAlert>>

    @GET
    suspend fun getGuideOffers(@Url url:String) : Response<List<GuidingOffer>>
}