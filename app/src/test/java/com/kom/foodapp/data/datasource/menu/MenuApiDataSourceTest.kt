package com.kom.foodapp.data.datasource.menu

import com.kom.foodapp.data.source.network.model.checkout.CheckoutRequestPayload
import com.kom.foodapp.data.source.network.model.checkout.CheckoutResponse
import com.kom.foodapp.data.source.network.model.checkout.CheckoutResponsePayload
import com.kom.foodapp.data.source.network.model.menu.MenuResponse
import com.kom.foodapp.data.source.network.services.FoodAppApiService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Written by Komang Yuda Saputra
 * Github : https://github.com/YudaSaputraa
 */
class MenuApiDataSourceTest {
    @MockK
    lateinit var service: FoodAppApiService
    private lateinit var dataSource: MenuDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dataSource = MenuApiDataSource(service)
    }

    @Test
    fun getMenuData() {
        runTest {
            val mockResponse = mockk<MenuResponse>(relaxed = true)
            coEvery { service.getMenu() } returns mockResponse
            val actualResult = dataSource.getMenuData()
            coVerify { service.getMenu() }
            assertEquals(mockResponse, actualResult)
        }
    }

    @Test
    fun createOrder() {
        runTest {
            val order1 =
                CheckoutResponsePayload(
                    name = "Bakso",
                    notes = null,
                    price = 5000,
                    qty = 5,
                )
            val order2 =
                CheckoutResponsePayload(
                    name = "Mie Goreng",
                    notes = "Gausah pake mie",
                    price = 12000,
                    qty = 2,
                )
            val ordersList = listOf(order1, order2)
            val total = 17000
            val username = "user123"
            val requestPayload =
                CheckoutRequestPayload(
                    orders = ordersList,
                    total = total,
                    username = username,
                )
            val mockResponse = mockk<CheckoutResponse>(relaxed = true)
            coEvery { service.createOrder(requestPayload) } returns mockResponse
            val actualResult = dataSource.createOrder(requestPayload)
            coVerify { service.createOrder(requestPayload) }
            assertEquals(mockResponse, actualResult)
        }
    }
}
