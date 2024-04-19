package com.kom.foodapp.data.source.network.model.checkout


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class CheckoutRequestPayload(
    @SerializedName("orders")
    val orders: List<CheckoutResponsePayload>,
    @SerializedName("total")
    val total: Int?,
    @SerializedName("username")
    val username: String?
)