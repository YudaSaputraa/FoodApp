package com.kom.foodapp.data.repository

import app.cash.turbine.test
import com.kom.foodapp.data.datasource.menu.MenuDataSource
import com.kom.foodapp.data.model.Cart
import com.kom.foodapp.data.model.User
import com.kom.foodapp.data.source.network.model.checkout.CheckoutResponse
import com.kom.foodapp.data.source.network.model.menu.MenuItemResponse
import com.kom.foodapp.data.source.network.model.menu.MenuResponse
import com.kom.foodapp.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Written by Komang Yuda Saputra
 * Github : https://github.com/YudaSaputraa
 */
class MenuRepositoryImplTest {
    @MockK
    lateinit var datasource: MenuDataSource

    @MockK
    lateinit var userRepository: UserRepository
    private lateinit var menuRepository: MenuRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        menuRepository = MenuRepositoryImpl(datasource, userRepository)
    }

    @Test
    fun getMenu() {
        val menu1 =
            MenuItemResponse(
                imgUrl = "exampleImgUrl",
                name = "Ayam Bakar",
                formattedPrice = "Rp10.000",
                price = 10000.0,
                menuDesc = "ayam bakar dibakar dengan arang",
                restoAddress = "jalan jalan",
            )
        val menu2 =
            MenuItemResponse(
                imgUrl = "exampleImgUrl1",
                name = "Ayam Goreng",
                formattedPrice = "Rp10.000",
                price = 10000.0,
                menuDesc = "ayam Goreng",
                restoAddress = "jalan jalan",
            )

        val mockListMenu = listOf(menu1, menu2)
        val mockResponse = mockk<MenuResponse>()
        every { mockResponse.data } returns mockListMenu
        runTest {
            coEvery { datasource.getMenuData(any()) } returns mockResponse
            menuRepository.getMenu("makanan").map {
                delay(100)
                it
            }.test {
                delay(2210)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                coVerify { datasource.getMenuData(any()) }
            }
        }
    }

    @Test
    fun getMenu_when_category_null() {
        val menu1 =
            MenuItemResponse(
                imgUrl = "exampleImgUrl",
                name = "Ayam Bakar",
                formattedPrice = "Rp10.000",
                price = 10000.0,
                menuDesc = "ayam bakar dibakar dengan arang",
                restoAddress = "jalan jalan",
            )
        val menu2 =
            MenuItemResponse(
                imgUrl = "exampleImgUrl1",
                name = "Ayam Goreng",
                formattedPrice = "Rp10.000",
                price = 10000.0,
                menuDesc = "ayam Goreng",
                restoAddress = "jalan jalan",
            )

        val mockListMenu = listOf(menu1, menu2)
        val mockResponse = mockk<MenuResponse>()
        every { mockResponse.data } returns mockListMenu
        runTest {
            coEvery { datasource.getMenuData(any()) } returns mockResponse
            menuRepository.getMenu().map {
                delay(100)
                it
            }.test {
                delay(2210)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                coVerify { datasource.getMenuData(any()) }
            }
        }
    }

    @Test
    fun createOrder() {
        runTest {
            val cart1 = Cart(1, "efeg", "Ayam Bakar", "imgUrl", 10000.0, 1, "Extra spicy")
            val cart2 = Cart(2, "Gedfef", "Ayam Goreng", "imgUrl", 10000.0, 2, "No chili")
            val items = listOf(cart1, cart2)

            val currentUser = User(id = "1", fullName = "John Doe", email = "john@example.com")
            coEvery { userRepository.getCurrentUser() } returns currentUser

            coEvery { datasource.createOrder(any()) } returns
                CheckoutResponse(
                    status = true,
                    message = "success",
                    code = 200,
                )

            menuRepository.createOrder(items).map {
                delay(100)
                it
            }.test {
                delay(201)
                val actualResult = expectMostRecentItem()
                assertTrue(actualResult is ResultWrapper.Success)
            }
            coVerify { userRepository.getCurrentUser() }
            coVerify { datasource.createOrder(any()) }
        }
    }
}
