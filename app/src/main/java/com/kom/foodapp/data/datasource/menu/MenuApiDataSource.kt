package com.kom.foodapp.data.datasource.menu

import com.kom.foodapp.data.source.network.model.menu.MenuResponse
import com.kom.foodapp.data.source.network.services.FoodAppApiService

/**
Written by Komang Yuda Saputra
Github : https://github.com/YudaSaputraa
 **/
class MenuApiDataSource(
    private val service: FoodAppApiService
) : MenuDataSource{
    override suspend fun getMenuData(categoryName: String?): MenuResponse {
        return service.getMenu(categoryName)
    }

}