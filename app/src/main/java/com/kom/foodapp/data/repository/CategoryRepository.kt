package com.kom.foodapp.data.repository

import com.kom.foodapp.data.datasource.category.CategoryDataSource
import com.kom.foodapp.data.mapper.toCategories
import com.kom.foodapp.data.model.Category
import com.kom.foodapp.utils.ResultWrapper
import com.kom.foodapp.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getCategories(): Flow<ResultWrapper<List<Category>>>
}

class CategoryRepositoryImpl(private val dataSource: CategoryDataSource) : CategoryRepository {
    override fun getCategories(): Flow<ResultWrapper<List<Category>>> {
        return proceedFlow {
            dataSource.getCategoryData().data.toCategories()
        }
    }
}
