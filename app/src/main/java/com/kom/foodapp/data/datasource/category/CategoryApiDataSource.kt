package com.kom.foodapp.data.datasource.category

import com.kom.foodapp.data.source.network.model.category.CategoryResponse
import com.kom.foodapp.data.source.network.services.FoodAppApiService

/**
Written by Komang Yuda Saputra
Github : https://github.com/YudaSaputraa
 **/
class CategoryApiDataSource(
    private val service: FoodAppApiService
) : CategoryDataSource {
    override suspend fun getCategoryData(): CategoryResponse {
        return service.getCategories()
    }
}