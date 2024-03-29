package com.kom.foodapp.data.repository

import com.kom.foodapp.data.datasource.menu.MenuDataSource
import com.kom.foodapp.data.model.Menu

interface MenuRepository {
    fun getMenu(): List<Menu>
}

class MenuRepositoryImpl(private val dataSource: MenuDataSource) : MenuRepository {
    override fun getMenu(): List<Menu> = dataSource.getMenuData()

}