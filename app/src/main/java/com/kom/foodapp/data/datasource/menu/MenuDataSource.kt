package com.kom.foodapp.data.datasource.menu

import com.kom.foodapp.data.model.Menu
import com.kom.foodapp.data.source.network.model.menu.MenuResponse


interface MenuDataSource {
  suspend  fun getMenuData(categoryName : String? = null): MenuResponse
}



