package com.example.guidemeguidesapp.interfaces

import com.example.guidemeguidesapp.dataModels.ExperienceReservation
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface IReservationServiceAPI {
    @GET
    suspend fun getGuideReservations(@Url url:String) : Response<List<ExperienceReservation>>

    @GET
    suspend fun getReservation(@Url url: String) : Response<ExperienceReservation>
}