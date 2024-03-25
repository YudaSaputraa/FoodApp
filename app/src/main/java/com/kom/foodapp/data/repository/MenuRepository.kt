package com.kom.foodapp.data.repository

import com.example.foodapp.model.Menu
import com.kom.foodapp.data.datasource.menu.MenuDataSource

interface MenuRepository {
    fun getMenu(): List<Menu>
}

class MenuRepositoryImpl(private val dataSource: MenuDataSource) : MenuRepository {
    override fun getMenu(): List<Menu> = dataSource.getMenuData()

}