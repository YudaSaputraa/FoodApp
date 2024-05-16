package com.kom.foodapp.presentation.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kom.foodapp.data.model.User
import com.kom.foodapp.data.repository.UserRepository
import com.kom.foodapp.tools.MainCoroutineRule
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

/**
 * Written by Komang Yuda Saputra
 * Github : https://github.com/YudaSaputraa
 */
class ProfileViewModelTest {
    @get:Rule
    val testRule: TestRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule: TestRule = MainCoroutineRule(UnconfinedTestDispatcher())

    @MockK
    lateinit var userRepository: UserRepository

    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel =
            spyk(
                ProfileViewModel(userRepository),
            )
        viewModel.isEditProfile.value = false
    }

    @Test
    fun isEditProfile() {
        assertFalse(viewModel.isEditProfile.value ?: true)
    }

    @Test
    fun isUserLoggedOut() {
        every { userRepository.doLogout() } returns true
        val result = viewModel.isUserLoggedOut()
        assertEquals(true, result)
        verify { userRepository.doLogout() }
    }

    @Test
    fun getCurrentUser() {
        val currentUser = mockk<User>()
        every { userRepository.getCurrentUser() } returns currentUser
        val result = viewModel.getCurrentUser()
        assertEquals(currentUser, result)
        verify { userRepository.getCurrentUser() }
    }

    @Test
    fun requestChangePasswordByEmail() {
        every { userRepository.reqChangePasswordByEmail() } returns true
        val result = viewModel.requestChangePasswordByEmail()
        assertEquals(true, result)
        verify { userRepository.reqChangePasswordByEmail() }
    }

    @Test
    fun updateProfile() {
        every { userRepository.updateProfile(any()) } returns
            flow {
                emit(ResultWrapper.Success(true))
            }
        viewModel.updateProfile("komang1234")
        verify { userRepository.updateProfile(any()) }
    }
}
