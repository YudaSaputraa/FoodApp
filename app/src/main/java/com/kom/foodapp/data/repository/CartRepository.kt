package com.kom.foodapp.data.repository

import com.kom.foodapp.data.datasource.cart.CartDataSource
import com.kom.foodapp.data.mapper.toCartEntity
import com.kom.foodapp.data.mapper.toCartList
import com.kom.foodapp.data.model.Cart
import com.kom.foodapp.data.model.Menu
import com.kom.foodapp.data.model.PriceItem
import com.kom.foodapp.data.source.local.database.entity.CartEntity
import com.kom.foodapp.utils.ResultWrapper
import com.kom.foodapp.utils.proceed
import com.kom.foodapp.utils.proceedFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.lang.Exception
import java.lang.IllegalStateException

interface CartRepository {
    fun createCart(
        menu: Menu,
        quantity: Int,
        notes: String? = null,
    ): Flow<ResultWrapper<Boolean>>

    fun decreaseCartItem(item: Cart): Flow<ResultWrapper<Boolean>>

    fun increaseCartItem(item: Cart): Flow<ResultWrapper<Boolean>>

    fun setCartNotes(item: Cart): Flow<ResultWrapper<Boolean>>

    fun deleteCart(item: Cart): Flow<ResultWrapper<Boolean>>

    fun deleteAllCarts(): Flow<ResultWrapper<Unit>>

    fun getUserCartData(): Flow<ResultWrapper<Pair<List<Cart>, Double>>>

    fun getCheckoutData(): Flow<ResultWrapper<Triple<List<Cart>, List<PriceItem>, Double>>>
}

class CartRepositoryImpl(private val cartDataSource: CartDataSource) : CartRepository {
    override fun createCart(
        menu: Menu,
        quantity: Int,
        notes: String?,
    ): Flow<ResultWrapper<Boolean>> {
        return menu.id?.let { menuId ->
            proceedFlow {
                val affectedRow =
                    cartDataSource.insertCart(
                        CartEntity(
                            menuId = menuId,
                            menuName = menu.name,
                            menuImgUrl = menu.imageUrl,
                            menuPrice = menu.price,
                            itemQuantity = quantity,
                            itemNotes = notes,
                        ),
                    )
                delay(1000)
                affectedRow > 0
            }
        } ?: flow {
            emit(ResultWrapper.Error(IllegalStateException("Menu ID doesn't exist")))
        }
    }

    override fun decreaseCartItem(item: Cart): Flow<ResultWrapper<Boolean>> {
        val modifiedCart =
            item.copy().apply {
                itemQuantity -= 1
            }
        return if (modifiedCart.itemQuantity <= 0) {
            proceedFlow { cartDataSource.deleteCart(item.toCartEntity()) > 0 }
        } else {
            proceedFlow { cartDataSource.updateCart(modifiedCart.toCartEntity()) > 0 }
        }
    }

    override fun increaseCartItem(item: Cart): Flow<ResultWrapper<Boolean>> {
        val modifiedCart =
            item.copy().apply {
                itemQuantity += 1
            }
        return proceedFlow { cartDataSource.updateCart(modifiedCart.toCartEntity()) > 0 }
    }

    override fun setCartNotes(item: Cart): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { cartDataSource.updateCart(item.toCartEntity()) > 0 }
    }

    override fun deleteCart(item: Cart): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { cartDataSource.deleteCart(item.toCartEntity()) > 0 }
    }

    override fun deleteAllCarts(): Flow<ResultWrapper<Unit>> {
        return proceedFlow {
            cartDataSource.deleteAll()
        }
    }

    override fun getUserCartData(): Flow<ResultWrapper<Pair<List<Cart>, Double>>> {
        return cartDataSource.getAllCarts()
            .map {
                proceed {
                    val result = it.toCartList()
                    val totalPrice = result.sumOf { it.menuPrice * it.itemQuantity }
                    Pair(result, totalPrice)
                }
            }.map {
                if (it.payload?.first?.isEmpty() == false) return@map it
                ResultWrapper.Empty(it.payload)
            }.catch {
                emit(ResultWrapper.Error(Exception(it)))
            }.onStart {
                emit(ResultWrapper.Loading())
                delay(2000)
            }
    }

    override fun getCheckoutData(): Flow<ResultWrapper<Triple<List<Cart>, List<PriceItem>, Double>>> {
        return cartDataSource.getAllCarts()
            .map {
                proceed {
                    val result = it.toCartList()
                    val priceItemList =
                        result.map {
                            PriceItem(it.menuName, it.menuPrice * it.itemQuantity)
                        }
                    val totalPrice = priceItemList.sumOf { it.total }
                    Triple(result, priceItemList, totalPrice)
                }
            }.map {
                if (it.payload?.first?.isEmpty() == false) return@map it
                ResultWrapper.Empty(it.payload)
            }.catch {
                emit(ResultWrapper.Error(Exception(it)))
            }.onStart {
                emit(ResultWrapper.Loading())
                delay(2000)
            }
    }
}
