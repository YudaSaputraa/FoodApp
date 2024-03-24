package com.kom.foodapp.data.datasource

import com.kom.foodapp.data.model.Category

interface CategoryDataSource {
    fun getCategoryData(): List<Category>
}

class CategoryDataSourceImpl() : CategoryDataSource {
    override fun getCategoryData(): List<Category> {
        return mutableListOf(
            Category(
                image = "",
                name = "Nasi"
            ),
            Category(
                image = "",
                name = "Mie"
            ),
            Category(
                image = "",
                name = "Snack"
            ),
            Category(
                image = "",
                name = "Minuman"
            ),
            Category(
                image = "",
                name = "Sayuran"
            ),
        )
    }

}