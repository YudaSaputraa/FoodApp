package com.kom.foodapp.data.source.network.model.checkout


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class CheckoutResponsePayload(
    @SerializedName("catatan")
    val notes: String?,
    @SerializedName("harga")
    val price: Int?,
    @SerializedName("nama")
    val name: String?,
    @SerializedName("qty")
    val qty: Int?
)