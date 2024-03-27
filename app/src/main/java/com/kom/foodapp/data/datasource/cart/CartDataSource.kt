package com.kom.foodapp.data.datasource.cart

import com.kom.foodapp.data.source.local.database.dao.CartDao
import com.kom.foodapp.data.source.local.database.entity.CartEntity
import kotlinx.coroutines.flow.Flow

interface CartDataSource {
    fun getAllCarts(): Flow<List<CartEntity>>
    suspend fun insertCart(cart: CartEntity): Long
    suspend fun updateCart(cart: CartEntity): Int
    suspend fun deleteCart(cart: CartEntity): Int
    suspend fun deleteAll()
}

class CartDatabaseDataSource(private val dao: CartDao) : CartDataSource {
    override fun getAllCarts(): Flow<List<CartEntity>> = dao.getAllCarts()
    override suspend fun insertCart(cart: CartEntity): Long = dao.insertCart(cart)
    override suspend fun updateCart(cart: CartEntity): Int = dao.updateCart(cart)
    override suspend fun deleteCart(cart: CartEntity): Int = dao.deleteCart(cart)
    override suspend fun deleteAll() = dao.deleteAll()

}