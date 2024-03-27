package com.kom.foodapp.data.mapper

import com.kom.foodapp.data.model.Cart
import com.kom.foodapp.data.source.local.database.entity.CartEntity

fun Cart?.toCartEntity() = CartEntity(
    id = this?.id,
    menuId = this?.menuId.orEmpty(),
    menuName = this?.menuName.orEmpty(),
    menuPrice = this?.menuPrice ?: 0.0,
    menuImgUrl = this?.menuImgUrl.orEmpty(),
    itemQuantity = this?.itemQuantity ?: 0
)

fun CartEntity?.toCart() = Cart(
    id = this?.id,
    menuId = this?.menuId.orEmpty(),
    menuName = this?.menuName.orEmpty(),
    menuPrice = this?.menuPrice ?: 0.0,
    menuImgUrl = this?.menuImgUrl.orEmpty(),
    itemQuantity = this?.itemQuantity ?: 0,
)

fun List<CartEntity?>.toCartList() = this.map { it.toCart() }