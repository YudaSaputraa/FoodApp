package com.kom.foodapp.data.model

import com.google.firebase.auth.FirebaseUser

/**
Written by Komang Yuda Saputra
Github : https://github.com/YudaSaputraa
 **/
data class User(
    val id: String,
    val fullName: String,
//    val username : String,
    val email: String,
//    val password : String,
//    val phoneNumber : String,
//    val imgUrl : String
)

fun FirebaseUser?.toUser() =
    this?.let {
        User(
            id = uid,
            fullName = this.displayName.orEmpty(),
            email = this.email.orEmpty(),
        )
    }
