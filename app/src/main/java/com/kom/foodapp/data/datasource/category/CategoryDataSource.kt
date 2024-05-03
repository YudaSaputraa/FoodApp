package com.kom.foodapp.data.datasource.category

import com.kom.foodapp.data.source.network.model.category.CategoryResponse

interface CategoryDataSource {
    suspend fun getCategoryData(): CategoryResponse
}
