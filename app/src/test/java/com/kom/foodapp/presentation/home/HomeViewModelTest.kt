package com.kom.foodapp.presentation.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kom.foodapp.data.model.Menu
import com.kom.foodapp.data.model.User
import com.kom.foodapp.data.repository.CartRepository
import com.kom.foodapp.data.repository.CategoryRepository
import com.kom.foodapp.data.repository.MenuRepository
import com.kom.foodapp.data.repository.UserPrefRepository
import com.kom.foodapp.data.repository.UserRepository
import com.kom.foodapp.tools.MainCoroutineRule
import com.kom.foodapp.tools.getOrAwaitValue
import com.kom.foodapp.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.lang.IllegalStateException

/**
 * Written by Komang Yuda Saputra
 * Github : https://github.com/YudaSaputraa
 */
class HomeViewModelTest {
    @get:Rule
    val testRule: TestRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule: TestRule = MainCoroutineRule(UnconfinedTestDispatcher())

    @MockK
    lateinit var categoryRepository: CategoryRepository

    @MockK
    lateinit var menuRepository: MenuRepository

    @MockK
    lateinit var userRepository: UserRepository

    @MockK
    lateinit var cartRepository: CartRepository

    @MockK
    lateinit var userPrefRepository: UserPrefRepository

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel =
            spyk(
                HomeViewModel(
                    categoryRepository,
                    menuRepository,
                    cartRepository,
                    userRepository,
                    userPrefRepository,
                ),
            )
    }

    @Test
    fun getMenuCountLiveData() {
    }

    @Test
    fun getMenu() {
        every { menuRepository.getMenu(any()) } returns
            flow {
                emit(ResultWrapper.Success(listOf(mockk(), mockk())))
            }

        val result = viewModel.getMenu().getOrAwaitValue()
        assertEquals(2, result.payload?.size)
        verify { menuRepository.getMenu(any()) }
    }

    @Test
    fun getCategories() {
        every { categoryRepository.getCategories() } returns
            flow {
                emit(ResultWrapper.Success(listOf(mockk(), mockk())))
            }

        val result = viewModel.getCategories().getOrAwaitValue()
        assertEquals(2, result.payload?.size)
        verify { categoryRepository.getCategories() }
    }

    @Test
    fun addItemToCart_success() {
        val menu1 =
            Menu(
                imageUrl = "exampleImgUrl",
                name = "Ayam Bakar",
                formattedPrice = "Rp10.000",
                price = 10000.0,
                desc = "ayam bakar dibakar dengan arang",
                locationUrl = "exampleLocationUrl",
                locationAddress = "jalan jalan",
            )
        every { cartRepository.createCart(any(), any()) } returns
            flow {
                emit(ResultWrapper.Success(true))
            }
        viewModel.addItemToCart(menu1)
        verify { cartRepository.createCart(menu1, 1) }
    }

    @Test
    fun addItemToCart_error() {
        val menu1 =
            Menu(
                imageUrl = "exampleImgUrl",
                name = "Ayam Bakar",
                formattedPrice = "Rp10.000",
                price = 10000.0,
                desc = "ayam bakar dibakar dengan arang",
                locationUrl = "exampleLocationUrl",
                locationAddress = "jalan jalan",
            )
        every { cartRepository.createCart(any(), any()) } returns
            flow {
                emit(ResultWrapper.Error(IllegalStateException("Add item error")))
            }
        viewModel.addItemToCart(menu1)
        verify { cartRepository.createCart(menu1, 1) }
    }

    @Test
    fun addItemToCart_empty() {
        val menu1 =
            Menu(
                imageUrl = "exampleImgUrl",
                name = "Ayam Bakar",
                formattedPrice = "Rp10.000",
                price = 10000.0,
                desc = "ayam bakar dibakar dengan arang",
                locationUrl = "exampleLocationUrl",
                locationAddress = "jalan jalan",
            )
        every { cartRepository.createCart(any(), any()) } returns
            flow {
                emit(ResultWrapper.Empty(false))
            }
        viewModel.addItemToCart(menu1)
        verify { cartRepository.createCart(menu1, 1) }
    }

    @Test
    fun getCurrentUser() {
        val user1 =
            User(
                id = "1",
                fullName = "Komang",
                email = "example@gmail.com",
            )
        every { userRepository.getCurrentUser() } returns user1

        val result = viewModel.getCurrentUser()
        assertEquals(user1, result)
        verify { userRepository.getCurrentUser() }
    }

    @Test
    fun userIsLoggedIn() {
        every { userRepository.isLoggedIn() } returns true
        val result = viewModel.userIsLoggedIn()
        assertEquals(true, result)
        verify { userRepository.isLoggedIn() }
    }

    @Test
    fun isUsingGridMode() {
        every { userPrefRepository.isUsingGridMode() } returns true
        val result = viewModel.isUsingGridMode()
        assertEquals(true, result)
        verify { userPrefRepository.isUsingGridMode() }
    }

    @Test
    fun setUsingGridMode() {
        every { userPrefRepository.setUsingGridMode(any()) } returns Unit
        val result = viewModel.setUsingGridMode(true)
        assertEquals(Unit, result)
        verify { userPrefRepository.setUsingGridMode(any()) }
    }

    @Test
    fun testMenuCountLiveData() {
        val mockMenu =
            Menu(
                imageUrl = "exampleImgUrl",
                name = "Ayam Bakar",
                formattedPrice = "Rp10.000",
                price = 10000.0,
                desc = "ayam bakar dibakar dengan arang",
                locationUrl = "exampleLocationUrl",
                locationAddress = "jalan jalan",
            )

        assert(viewModel.menuCountLiveData.value == 0)
        viewModel.addItemToCart(mockMenu)
        assert(viewModel.menuCountLiveData.value == 1)
    }
}
