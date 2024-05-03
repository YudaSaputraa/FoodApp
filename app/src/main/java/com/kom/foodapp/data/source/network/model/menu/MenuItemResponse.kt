package com.kom.foodapp.data.source.network.model.menu

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
Written by Komang Yuda Saputra
Github : https://github.com/YudaSaputraa
 **/
@Keep
data class MenuItemResponse(
    @SerializedName("image_url")
    val imgUrl: String?,
    @SerializedName("nama")
    val name: String?,
    @SerializedName("harga_format")
    val formattedPrice: String?,
    @SerializedName("harga")
    val price: Double?,
    @SerializedName("detail")
    val menuDesc: String?,
    @SerializedName("alamat_resto")
    val restoAddress: String?,
)
