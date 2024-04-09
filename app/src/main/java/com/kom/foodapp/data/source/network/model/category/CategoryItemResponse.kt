package com.kom.foodapp.data.source.network.model.category

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
Written by Komang Yuda Saputra
Github : https://github.com/YudaSaputraa
 **/
@Keep
data class CategoryItemResponse(
    @SerializedName("image_url")
    val imgUrl: String?,
    @SerializedName("nama")
    val name: String?

)
