package com.kom.foodapp.data.repository

import com.kom.foodapp.data.datasource.category.CategoryDataSource
import com.kom.foodapp.data.model.Category

interface CategoryRepository {
    fun getCategories() : List<Category>
}

class CategoryRepositoryImpl(private val dataSource:CategoryDataSource) : CategoryRepository{
    override fun getCategories(): List<Category>  = dataSource.getCategoryData()

}