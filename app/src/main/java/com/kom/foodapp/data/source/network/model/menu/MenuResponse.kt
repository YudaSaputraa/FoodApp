package com.kom.foodapp.data.source.network.model.menu

import com.google.gson.annotations.SerializedName
import com.kom.foodapp.data.source.network.model.category.CategoryItemResponse

/**
Written by Komang Yuda Saputra
Github : https://github.com/YudaSaputraa
 **/
data class MenuResponse(
    @SerializedName("status")
    val status : Boolean?,
    @SerializedName("code")
    val code : Int?,
    @SerializedName("message")
    val message : String?,
    @SerializedName("data")
    val data : List<MenuItemResponse>,

    )
