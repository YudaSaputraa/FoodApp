package com.kom.foodapp.data.model

import java.util.UUID

data class Profile(
    var id: String = UUID.randomUUID().toString(),
    var image: String,
    var name: String,
    var email : String,
    var phoneNumber: String,
    var password: String,
    var locationAddress: String,
)
