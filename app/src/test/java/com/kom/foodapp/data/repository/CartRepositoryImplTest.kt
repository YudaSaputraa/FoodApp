package com.kom.foodapp.data.repository

import app.cash.turbine.test
import com.kom.foodapp.data.datasource.cart.CartDataSource
import com.kom.foodapp.data.model.Cart
import com.kom.foodapp.data.model.Menu
import com.kom.foodapp.data.model.PriceItem
import com.kom.foodapp.data.source.local.database.entity.CartEntity
import com.kom.foodapp.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.lang.IllegalStateException

/**
 * Written by Komang Yuda Saputra
 * Github : https://github.com/YudaSaputraa
 */
class CartRepositoryImplTest {
    @MockK
    lateinit var datasource: CartDataSource
    private lateinit var repository: CartRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = CartRepositoryImpl(datasource)
    }

    @Test
    fun createCart_success() {
        val mockProduct = mockk<Menu>(relaxed = true)
        every { mockProduct.id } returns "123"
        coEvery { datasource.insertCart(any()) } returns 1
        runTest {
            repository.createCart(mockProduct, 3, "fegeref")
                .map {
                    delay(100)
                    it
                }.test {
                    delay(2201)
                    val actualData = expectMostRecentItem()
                    assertTrue(actualData is ResultWrapper.Success)
                    assertEquals(true, actualData.payload)
                    coVerify { datasource.insertCart(any()) }
                }
        }
    }

    @Test
    fun createCart_success_on_notes_null() {
        val mockProduct = mockk<Menu>(relaxed = true)
        every { mockProduct.id } returns "123"
        coEvery { datasource.insertCart(any()) } returns 1
        runTest {
            repository.createCart(mockProduct, 3)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(2201)
                    val actualData = expectMostRecentItem()
                    assertTrue(actualData is ResultWrapper.Success)
                    assertEquals(true, actualData.payload)
                    coVerify { datasource.insertCart(any()) }
                }
        }
    }

    @Test
    fun createCart_error_when_id_null() {
        val mockProduct = mockk<Menu>(relaxed = true)
        every { mockProduct.id } returns null
        coEvery { datasource.insertCart(any()) } returns 1
        runTest {
            repository.createCart(mockProduct, 3, "kegerfef")
                .map {
                    delay(100)
                    it
                }.test {
                    delay(2010)
                    val actualData = expectMostRecentItem()
                    assertTrue(actualData is ResultWrapper.Error)
                    coVerify(atLeast = 0) { datasource.insertCart(any()) }
                }
        }
    }

    @Test
    fun createCart_loading() {
        val mockProduct = mockk<Menu>(relaxed = true)
        every { mockProduct.id } returns "123"
        coEvery { datasource.insertCart(any()) } returns 1
        runTest {
            repository.createCart(mockProduct, 3, "fegeref")
                .map {
                    delay(100)
                    it
                }.test {
                    delay(101)
                    val actualData = expectMostRecentItem()
                    assertTrue(actualData is ResultWrapper.Loading)
                    coVerify { datasource.insertCart(any()) }
                }
        }
    }

    @Test
    fun createCart_error() {
        val mockProduct = mockk<Menu>(relaxed = true)
        every { mockProduct.id } returns "123"
        coEvery { datasource.insertCart(any()) } throws IllegalStateException("Crate Cart Error")
        runTest {
            repository.createCart(mockProduct, 3, "fegeref")
                .map {
                    delay(100)
                    it
                }.test {
                    delay(2201)
                    val actualData = expectMostRecentItem()
                    assertTrue(actualData is ResultWrapper.Error)
                    coVerify { datasource.insertCart(any()) }
                }
        }
    }

    @Test
    fun createCart_empty() {
        runTest {
            val mockList = listOf<CartEntity>()
            val mockFlow = flow { emit(mockList) }
            every { datasource.getAllCarts() } returns mockFlow
            repository.getUserCartData().map {
                delay(100)
                it
            }.test {
                delay(2210)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Empty)
                verify { datasource.getAllCarts() }
            }
        }
    }

    @Test
    fun decreaseCart_when_quantity_more_than_0() {
        val mockCart =
            Cart(
                id = 1,
                menuId = "gelrkelge",
                menuName = "Mie Goreng",
                menuImgUrl = "exampleImgUrl1",
                menuPrice = 10000.0,
                itemQuantity = 2,
                itemNotes = "jangan digoreng",
            )

        coEvery { datasource.updateCart(any()) } returns 1
        runTest {
            repository.decreaseCartItem(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                coVerify(atLeast = 0) { datasource.deleteCart(any()) }
                coVerify(atLeast = 1) { datasource.updateCart(any()) }
            }
        }
    }

    @Test
    fun decreaseCart_when_quantity_less_than_1() {
        val mockCart =
            Cart(
                id = 2,
                menuId = "geffw4",
                menuName = "Mie Kuah",
                menuImgUrl = "exampleImgUrl2",
                menuPrice = 10000.0,
                itemQuantity = 0,
            )
        coEvery { datasource.deleteCart(any()) } returns 1
        coEvery { datasource.updateCart(any()) } returns 1
        runTest {
            repository.decreaseCartItem(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                coVerify { datasource.deleteCart(any()) }
                coVerify(atLeast = 0) { datasource.updateCart(any()) }
            }
        }
    }

    @Test
    fun increaseCartItem() {
        val mockCart =
            Cart(
                id = 1,
                menuId = "geffw4",
                menuName = "Mie Kuah",
                menuImgUrl = "exampleImgUrl2",
                menuPrice = 10000.0,
                itemQuantity = 1,
            )
        coEvery { datasource.updateCart(any()) } returns 1
        runTest {
            repository.increaseCartItem(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                coVerify(atLeast = 1) { datasource.updateCart(any()) }
            }
        }
    }

    @Test
    fun setCartNotes() {
        val mockCart =
            Cart(
                id = 1,
                menuId = "gelrkelge",
                menuName = "Mie Goreng",
                menuImgUrl = "exampleImgUrl1",
                menuPrice = 10000.0,
                itemQuantity = 2,
                itemNotes = "jangan digoreng",
            )
        coEvery { datasource.updateCart(any()) } returns 1
        runTest {
            repository.setCartNotes(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                coVerify { datasource.updateCart(any()) }
            }
        }
    }

    @Test
    fun deleteCart() {
        val mockCart =
            Cart(
                id = 1,
                menuId = "gelrkelge",
                menuName = "Mie Goreng",
                menuImgUrl = "exampleImgUrl1",
                menuPrice = 10000.0,
                itemQuantity = 2,
                itemNotes = "jangan digoreng",
            )
        coEvery { datasource.deleteCart(any()) } returns 1
        runTest {
            val result =
                repository.deleteCart(mockCart).map {
                    delay(100)
                    it
                }.test {
                    delay(210)
                    val actualResult = expectMostRecentItem()
                    assertTrue(actualResult is ResultWrapper.Success)
                }
            coVerify { datasource.deleteCart(any()) }
        }
    }

    @Test
    fun deleteAllCarts() {
        coEvery { datasource.deleteAll() } returns Unit
        runTest {
            val result =
                repository.deleteAllCarts().map {
                    delay(100)
                    it
                }.test {
                    delay(210)
                    val actualResult = expectMostRecentItem()
                    assertTrue(actualResult is ResultWrapper.Success)
                }
            coVerify { datasource.deleteAll() }
        }
    }

    @Test
    fun getUserCartData_success() {
        val entity1 =
            CartEntity(
                id = 1,
                menuImgUrl = "ExampleImgUrl1",
                menuName = "Nasi goreng",
                menuPrice = 10000.0,
                itemQuantity = 2,
                itemNotes = "feogjeo",
            )
        val entity2 =
            CartEntity(
                id = 2,
                menuImgUrl = "ExampleImgUrl2",
                menuName = "Nasi ayam",
                menuPrice = 10000.0,
                itemQuantity = 2,
                itemNotes = null,
            )
        val mockList = listOf(entity1, entity2)
        val mockFlow =
            flow {
                emit(mockList)
            }
        every { datasource.getAllCarts() } returns mockFlow
        runTest {
            repository.getUserCartData().map {
                delay(100)
                it
            }.test {
                delay(2201)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                assertEquals(mockList.size, actualData.payload?.first?.size)
                assertEquals(40000.0, actualData.payload?.second)
                verify { datasource.getAllCarts() }
            }
        }
    }

    @Test
    fun getUserCartData_loading() {
        val entity1 =
            CartEntity(
                id = 1,
                menuImgUrl = "ExampleImgUrl1",
                menuName = "Nasi goreng",
                menuPrice = 10000.0,
                itemQuantity = 2,
                itemNotes = "feogjeo",
            )
        val entity2 =
            CartEntity(
                id = 2,
                menuImgUrl = "ExampleImgUrl2",
                menuName = "Nasi ayam",
                menuPrice = 10000.0,
                itemQuantity = 2,
                itemNotes = null,
            )
        val mockList = listOf(entity1, entity2)
        val mockFlow =
            flow {
                emit(mockList)
            }
        every { datasource.getAllCarts() } returns mockFlow
        runTest {
            repository.getUserCartData().map {
                delay(100)
                it
            }.test {
                delay(2111)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Loading)
                verify { datasource.getAllCarts() }
            }
        }
    }

    @Test
    fun getUserCartData_error() {
        every { datasource.getAllCarts() } returns
            flow {
                throw IllegalStateException("checkoutError")
            }
        runTest {
            repository.getUserCartData().map {
                delay(100)
                it
            }.test {
                delay(2210)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Error)
                verify { datasource.getAllCarts() }
            }
        }
    }

    @Test
    fun getUserCartData_empty() {
        val mockList = listOf<CartEntity>()
        val mockFlow = flow { emit(mockList) }
        every { datasource.getAllCarts() } returns mockFlow
        runTest {
            repository.getUserCartData().map {
                delay(100)
                it
            }.test {
                delay(2210)
                val actualResult = expectMostRecentItem()
                assertTrue(actualResult is ResultWrapper.Empty)
                assertEquals(true, actualResult.payload?.first?.isEmpty())
                verify { datasource.getAllCarts() }
            }
        }
    }

    @Test
    fun getCheckoutData_success() {
        val entity1 =
            CartEntity(
                id = 1,
                menuImgUrl = "ExampleImgUrl1",
                menuName = "Nasi goreng",
                menuPrice = 10000.0,
                itemQuantity = 2,
                itemNotes = "feogjeo",
            )
        val entity2 =
            CartEntity(
                id = 2,
                menuImgUrl = "ExampleImgUrl2",
                menuName = "Nasi ayam",
                menuPrice = 10000.0,
                itemQuantity = 2,
                itemNotes = null,
            )
        val mockPriceList =
            listOf(PriceItem("Nasi goreng", 20000.0), PriceItem("Nasi ayam", 20000.0))
        val mockList = listOf(entity1, entity2)
        val mockFlow = flow { emit(mockList) }
        every { datasource.getAllCarts() } returns mockFlow
        runTest {
            repository.getCheckoutData().map {
                delay(100)
                it
            }.test {
                delay(2201)
                val actualResult = expectMostRecentItem()
                assertTrue(actualResult is ResultWrapper.Success)
                assertEquals(mockList.size, actualResult.payload?.first?.size)
                assertEquals(mockPriceList, actualResult.payload?.second)
                assertEquals(40000.0, actualResult.payload?.third)
                verify { datasource.getAllCarts() }
            }
        }
    }

    @Test
    fun getCheckoutData_loading() {
        val mockList = listOf<CartEntity>()
        val mockFlow = flow { emit(mockList) }
        every { datasource.getAllCarts() } returns mockFlow
        runTest {
            repository.getCheckoutData().map {
                delay(100)
                it
            }.test {
                delay(1110)
                val actualResult = expectMostRecentItem()
                assertTrue(actualResult is ResultWrapper.Loading)
                verify { datasource.getAllCarts() }
            }
        }
    }

    @Test
    fun getCheckoutData_error() {
        every { datasource.getAllCarts() } returns
            flow {
                throw IllegalStateException("Checkout Error")
            }
        runTest {
            repository.getCheckoutData().map {
                delay(100)
                it
            }.test {
                delay(2210)
                val actualResult = expectMostRecentItem()
                assertTrue(actualResult is ResultWrapper.Error)
                verify { datasource.getAllCarts() }
            }
        }
    }

    @Test
    fun getCheckoutData_empty() {
        val mockList = listOf<CartEntity>()
        val mockFlow = flow { emit(mockList) }
        every { datasource.getAllCarts() } returns mockFlow
        runTest {
            repository.getCheckoutData().map {
                delay(100)
                it
            }.test {
                delay(2210)
                val actualResult = expectMostRecentItem()
                assertTrue(actualResult is ResultWrapper.Empty)
                assertEquals(true, actualResult.payload?.first?.isEmpty())
                verify { datasource.getAllCarts() }
            }
        }
    }
}
