package com.kom.foodapp.data.source.network.model.checkout

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
Written by Komang Yuda Saputra
Github : https://github.com/YudaSaputraa
 **/
@Keep
data class CheckoutResponse(
    @SerializedName("status")
    val status: Boolean?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("code")
    val code: Int?


)
