package com.kom.foodapp.data.datasource.menu

import com.kom.foodapp.data.model.Menu


interface MenuDataSource {
    fun getMenuData(): List<Menu>
}



