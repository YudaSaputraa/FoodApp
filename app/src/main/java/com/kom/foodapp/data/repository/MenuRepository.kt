package com.kom.foodapp.data.repository

import com.kom.foodapp.data.datasource.menu.MenuDataSource
import com.kom.foodapp.data.mapper.toMenus
import com.kom.foodapp.data.model.Menu
import com.kom.foodapp.utils.ResultWrapper
import com.kom.foodapp.utils.proceedFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

interface MenuRepository {
    fun getMenu(categoryName: String? = null): Flow<ResultWrapper<List<Menu>>>
}

class MenuRepositoryImpl(private val dataSource: MenuDataSource) : MenuRepository {
    override fun getMenu(categoryName: String?): Flow<ResultWrapper<List<Menu>>> {
        return flow {
            emit(ResultWrapper.Loading())
            delay(1000)
            val menuData = dataSource.getMenuData(categoryName).data.toMenus()
            emit(ResultWrapper.Success(menuData))
        }
    }
}