package com.kom.foodapp.data.model

data class Cart(
    var id: Int? = null,
    var menuId: String? = null,
    var menuName: String,
    var menuImgUrl: String,
    var menuPrice: Double,
    var itemQuantity: Int = 0,
    var itemNotes: String? = null,
)
