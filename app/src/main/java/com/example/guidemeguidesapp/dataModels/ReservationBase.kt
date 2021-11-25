package com.example.guidemeguidesapp.dataModels

import java.util.*

sealed class ReservationBase {
    var id: String = ""
    var touristUserId: String = ""
    var guideUserId: String = ""
    var guideExperinceId: String = ""
    var guideFirstName: String = ""
    var guideLastName: String = ""
    var touristFirstName: String = ""
    var touristLastName: String = ""
    var fromDate: Date = Date()
    var toDate: Date = Date()
    var price: Float = 0.0f
    var address: Address = Address()
}