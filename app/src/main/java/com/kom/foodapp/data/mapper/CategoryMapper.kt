package com.kom.foodapp.data.mapper

import com.kom.foodapp.data.model.Category
import com.kom.foodapp.data.source.network.model.category.CategoryItemResponse

/**
Written by Komang Yuda Saputra
Github : https://github.com/YudaSaputraa
 **/
fun CategoryItemResponse?.toCategory() =
    Category(
        imageUrl = this?.imgUrl.orEmpty(),
        name = this?.name.orEmpty()
    )

fun Collection<CategoryItemResponse>?.toCategories() =
    this?.map { it.toCategory() } ?: listOf()