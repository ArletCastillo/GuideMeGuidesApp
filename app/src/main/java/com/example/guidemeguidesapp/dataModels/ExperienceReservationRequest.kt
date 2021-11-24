package com.example.guidemeguidesapp.dataModels

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

data class ExperienceReservationRequest(
    var reservationStatus: ReservationStatus
) : ReservationBase()

enum class ReservationStatus : JsonSerializer<ReservationStatus> {
    PENDING,
    ACCEPTED,
    REJECTED;

    override fun serialize(
        src: ReservationStatus?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement? {
        return JsonObject().apply { addProperty(src?.name, src?.ordinal) }
    }
}