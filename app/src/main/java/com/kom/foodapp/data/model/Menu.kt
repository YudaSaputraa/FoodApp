package com.kom.foodapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Menu(
    var id: String? = UUID.randomUUID().toString(),
    var imageUrl: String,
    var name: String,
    var formattedPrice: String,
    var price: Double,
    val desc: String,
    val locationUrl: String,
    val locationAddress: String,
) : Parcelable
