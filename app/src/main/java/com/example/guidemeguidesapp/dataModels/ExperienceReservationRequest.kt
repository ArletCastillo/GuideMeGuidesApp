package com.example.guidemeguidesapp.dataModels

data class ExperienceReservationRequest(
    var reservationStatus: ReservationStatus
) : ReservationBase()

enum class ReservationStatus {
    PENDING,
    ACCEPTED,
    REJECTED
}