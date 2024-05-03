package com.kom.foodapp.data.repository

import com.kom.foodapp.data.datasource.category.CategoryDataSource
import com.kom.foodapp.data.mapper.toCategories
import com.kom.foodapp.data.model.Category
import com.kom.foodapp.utils.ResultWrapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface CategoryRepository {
    fun getCategories(): Flow<ResultWrapper<List<Category>>>
}

class CategoryRepositoryImpl(private val dataSource: CategoryDataSource) : CategoryRepository {
    override fun getCategories(): Flow<ResultWrapper<List<Category>>> {
        return flow {
            emit(ResultWrapper.Loading())
            delay(1000)
            val categoryData = dataSource.getCategoryData().data.toCategories()
            emit(ResultWrapper.Success(categoryData))
        }
    }
}
