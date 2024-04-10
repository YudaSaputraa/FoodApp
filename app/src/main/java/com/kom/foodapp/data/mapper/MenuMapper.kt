package com.kom.foodapp.data.mapper

import android.view.MenuItem
import com.kom.foodapp.data.model.Menu
import com.kom.foodapp.data.source.network.model.menu.MenuItemResponse

/**
Written by Komang Yuda Saputra
Github : https://github.com/YudaSaputraa
 **/

fun MenuItemResponse?.toMenu() =
    Menu(
        name = this?.name.orEmpty(),
        formattedPrice = this?.formattedPrice.orEmpty(),
        price = this?.price ?: 0.0,
        imageUrl = this?.imgUrl.orEmpty(),
        desc = this?.menuDesc.orEmpty(),
        locationAddress = this?.restoAddress.orEmpty(),
        locationUrl = "https://maps.app.goo.gl/h4wQKqaBuXzftGK77"
    )

fun Collection<MenuItemResponse>?.toMenus() =
    this?.map { it.toMenu() } ?: listOf()