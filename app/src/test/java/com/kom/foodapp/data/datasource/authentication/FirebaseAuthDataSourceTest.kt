package com.kom.foodapp.data.datasource.authentication

import com.google.firebase.auth.FirebaseUser
import com.kom.foodapp.data.source.firebase.FirebaseService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Written by Komang Yuda Saputra
 * Github : https://github.com/YudaSaputraa
 */
class FirebaseAuthDataSourceTest {
    @MockK
    lateinit var service: FirebaseService
    private lateinit var dataSource: AuthDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dataSource = FirebaseAuthDataSource(service)
    }

    @Test
    fun doLogin() {
        runTest {
            coEvery { dataSource.doLogin(any(), any()) } returns true
            val actualResult = service.doLogin("komang@gmail.com", "Komangyuda06")
            coVerify { dataSource.doLogin(any(), any()) }
            assertEquals(true, actualResult)
        }
    }

    @Test
    fun doRegister() {
        runTest {
            coEvery { dataSource.doRegister(any(), any(), any()) } returns true
            val actualResult = service.doRegister("Komang Yuda", "komang@gmail.com", "Komangyuda06")
            coVerify { dataSource.doRegister(any(), any(), any()) }
            assertEquals(true, actualResult)
        }
    }

    @Test
    fun updateProfile() {
        runTest {
            coEvery { dataSource.updateProfile(any()) } returns true
            val actualResult = service.updateProfile("Yuda Komang")
            coVerify { dataSource.updateProfile(any()) }
            assertEquals(true, actualResult)
        }
    }

    @Test
    fun updatePassword() {
        runTest {
            coEvery { dataSource.updatePassword(any()) } returns true
            val actualResult = service.updatePassword("yuda123")
            coVerify { dataSource.updatePassword(any()) }
            assertEquals(true, actualResult)
        }
    }

    @Test
    fun updateEmail() {
        runTest {
            coEvery { dataSource.updateEmail(any()) } returns true
            val actualResult = service.updateEmail("yudakomang@gmail.com")
            coVerify { dataSource.updateEmail(any()) }
            assertEquals(true, actualResult)
        }
    }

    @Test
    fun reqChangePasswordByEmailWithoutLogin() {
        runTest {
            coEvery { dataSource.reqChangePasswordByEmailWithoutLogin(any()) } returns true
            val actualResult = service.reqChangePasswordByEmailWithoutLogin("yudakomang@gmail.com")
            coVerify { dataSource.reqChangePasswordByEmailWithoutLogin(any()) }
            assertEquals(true, actualResult)
        }
    }

    @Test
    fun reqChangePasswordByEmail() {
        runTest {
            every { dataSource.reqChangePasswordByEmail() } returns true
            val actualResult = service.reqChangePasswordByEmail()
            verify { dataSource.reqChangePasswordByEmail() }
            assertEquals(true, actualResult)
        }
    }

    @Test
    fun doLogout() {
        runTest {
            every { dataSource.doLogout() } returns true
            val actualResult = service.doLogout()
            verify { dataSource.doLogout() }
            assertEquals(true, actualResult)
        }
    }

    @Test
    fun isLoggedIn() {
        runTest {
            every { dataSource.isLoggedIn() } returns true
            val actualResult = service.isLoggedIn()
            verify { dataSource.isLoggedIn() }
            assertEquals(true, actualResult)
        }
    }

    @Test
    fun getCurrentUser() {
        runTest {
            val firebaseUser = mockk<FirebaseUser>()
            every { firebaseUser.uid } returns "123"
            every { firebaseUser.displayName } returns "Komang"
            every { firebaseUser.email } returns "komang@gmail.com"
            every { service.getCurrentUser() } returns firebaseUser

            val result = dataSource.getCurrentUser()
            assertEquals("123", result?.id)
            assertEquals("Komang", result?.fullName)
            assertEquals("komang@gmail.com", result?.email)
            verify { service.getCurrentUser() }
        }
    }
}
