package com.kom.foodapp.data.source.network.model.category

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
Written by Komang Yuda Saputra
Github : https://github.com/YudaSaputraa
 **/
@Keep
data class CategoryResponse (
    @SerializedName("status")
    val status : Boolean?,
    @SerializedName("code")
    val code : Int?,
    @SerializedName("message")
    val message : String?,
    @SerializedName("data")
    val data : List<CategoryItemResponse>,


    )