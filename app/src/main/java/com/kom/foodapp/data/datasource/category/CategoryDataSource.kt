package com.kom.foodapp.data.datasource.category

import com.kom.foodapp.data.model.Category

interface CategoryDataSource {
    fun getCategoryData(): List<Category>
}
