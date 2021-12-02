package com.example.guidemeguidesapp.dataModels

data class ExperienceReservation(
    var description: String = "",
    var experienceTags: List<String> = emptyList(),
    var experienceRating: Review = Review()
) : ReservationBase()