package com.kom.foodapp.data.datasource.menu

import com.example.foodapp.model.Menu

interface MenuDataSource {
    fun getMenuData(): List<Menu>
}



