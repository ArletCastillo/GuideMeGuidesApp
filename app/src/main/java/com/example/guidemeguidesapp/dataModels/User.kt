package com.example.guidemeguidesapp.dataModels

import com.example.guidemeguidesapp.dataModels.Review
import java.util.*

/**
 * Data class that represents an user in GuideMe
 */
data class User (
    var id: String = "",
    var firebaseUserId: String = "",
    var username: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var email: String = "",
    var sex: String = "",
    var birthdate: Date = Date(),
    var address: Address = Address(),
    var phone: String = "",
    var aboutUser: String = "",
    var profilePhotoUrl: String = "",
    var roles: List<String> = emptyList(),
    var reviews: List<Review> = emptyList(),
    var wishlist: MutableList<String> = mutableListOf(),
    var languages: MutableList<String> = mutableListOf(),
    var country: String = ""
)