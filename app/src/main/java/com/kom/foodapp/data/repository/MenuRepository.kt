package com.kom.foodapp.data.repository

import com.kom.foodapp.data.datasource.menu.MenuDataSource
import com.kom.foodapp.data.mapper.toMenus
import com.kom.foodapp.data.model.Cart
import com.kom.foodapp.data.model.Menu
import com.kom.foodapp.data.source.network.model.checkout.CheckoutRequestPayload
import com.kom.foodapp.data.source.network.model.checkout.CheckoutResponsePayload
import com.kom.foodapp.utils.ResultWrapper
import com.kom.foodapp.utils.proceedFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface MenuRepository {
    fun getMenu(categoryName: String? = null): Flow<ResultWrapper<List<Menu>>>

    fun createOrder(items: List<Cart>): Flow<ResultWrapper<Boolean>>
}

class MenuRepositoryImpl(
    private val dataSource: MenuDataSource,
    private val userRepository: UserRepository,
) : MenuRepository {
    override fun getMenu(categoryName: String?): Flow<ResultWrapper<List<Menu>>> {
        return flow {
            emit(ResultWrapper.Loading())
            delay(1000)
            val menuData = dataSource.getMenuData(categoryName).data.toMenus()
            emit(ResultWrapper.Success(menuData))
        }
    }

    override fun createOrder(items: List<Cart>): Flow<ResultWrapper<Boolean>> {
        return proceedFlow {
            val currentUser = userRepository.getCurrentUser()
            val total = items.sumOf { it.menuPrice }
            dataSource.createOrder(
                CheckoutRequestPayload(
                    total = total.toInt(),
                    username = currentUser?.fullName,
                    orders =
                        items.map {
                            CheckoutResponsePayload(
                                notes = it.itemNotes,
                                price = it.menuPrice.toInt(),
                                name = it.menuName,
                                qty = it.itemQuantity,
                            )
                        },
                ),
            ).status ?: false
        }
    }
}
