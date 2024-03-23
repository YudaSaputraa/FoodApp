package com.example.foodapp.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import java.util.UUID

data class Menu(
    var id: String = UUID.randomUUID().toString(),
    var image: String,
    var name: String,
    var price: Double,
    val desc: String,
    val locationUrl: String,
    val locationAddress: String,

    )
