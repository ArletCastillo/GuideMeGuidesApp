package com.example.guidemeguidesapp.interfaces

import com.example.guidemeguidesapp.dataModels.ExperienceReservation
import com.example.guidemeguidesapp.dataModels.ExperienceReservationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Url

interface IReservationServiceAPI {
    @GET
    suspend fun getGuideReservations(@Url url:String) : Response<List<ExperienceReservation>>

    @GET
    suspend fun getReservationRequestsForGuide(@Url url:String) : Response<List<ExperienceReservationRequest>>

    @GET
    suspend fun getReservation(@Url url: String) : Response<ExperienceReservation>

    @GET
    suspend fun acceptReservationRequest(@Url url: String) : Response<Unit>

    @GET
    suspend fun rejectReservationRequest(@Url url: String) : Response<Unit>

    @GET
    suspend fun getPastExperiencesForGuide(@Url url:String) : Response<List<ExperienceReservation>>

    @PUT
    suspend fun rateExperience(@Url url:String, @Body body: ExperienceReservation) : Response<Unit>

    @PUT
    suspend fun rateTourist(@Url url:String, @Body body: ExperienceReservation) : Response<Unit>

}