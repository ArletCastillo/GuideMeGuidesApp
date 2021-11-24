package com.example.guidemeguidesapp.interfaces

import com.example.guidemeguidesapp.dataModels.User
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

/**
 * API interface for the Authentication Service
 */
interface
IAuthenticationServiceApi {
    @POST
    suspend fun post(@Url url:String, @Body body: User) : Response<Unit>

    @GET
    suspend fun getByEmail(@Url url:String) : Response<User>

    @GET
    suspend fun getByFirebaseId(@Url url:String) : Response<User>

    @GET
    suspend fun getById(@Url url: String) : Response<User>

    @PUT
    suspend fun update(@Url url:String, @Body body: User) : Response<Unit>

    @Multipart
    @POST
    suspend fun update_photo(@Url url:String,
                       @Part file: MultipartBody.Part) : Response<Unit>
}