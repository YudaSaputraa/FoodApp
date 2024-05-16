package com.kom.foodapp.data.repository

import app.cash.turbine.test
import com.kom.foodapp.data.datasource.authentication.AuthDataSource
import com.kom.foodapp.data.model.User
import com.kom.foodapp.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException

/**
 * Written by Komang Yuda Saputra
 * Github : https://github.com/YudaSaputraa
 */
class UserRepositoryImplTest {
    @MockK
    lateinit var dataSource: AuthDataSource
    private lateinit var repository: UserRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = UserRepositoryImpl(dataSource)
    }

    @Test
    fun doLogin_success() {
        val email = "komang@@gmail.com"
        val password = "komang234354"
        coEvery { dataSource.doLogin(any(), any()) } returns true
        runTest {
            repository.doLogin(email, password).map {
                delay(100)
                it
            }.test {
                delay(201)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                assertEquals(true, actualData.payload)
                coVerify { dataSource.doLogin(any(), any()) }
            }
        }
    }

    @Test
    fun doLogin_loading() {
        val email = "komang@@gmail.com"
        val password = "komang234354"
        coEvery { dataSource.doLogin(any(), any()) } returns true
        runTest {
            repository.doLogin(email, password).map {
                delay(100)
                it
            }.test {
                delay(111)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Loading)
                coVerify { dataSource.doLogin(any(), any()) }
            }
        }
    }

    @Test
    fun doLogin_error() {
        val email = "komang@@gmail.com"
        val password = "komang234354"
        coEvery { dataSource.doLogin(any(), any()) } throws IOException("Login failed")
        runTest {
            repository.doLogin(email, password).map {
                delay(100)
                it
            }.test {
                delay(210)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Error)
                coVerify { dataSource.doLogin(any(), any()) }
            }
        }
    }

    @Test
    fun doRegister_success() {
        val fullName = "Komangyuda"
        val email = "komang@@gmail.com"
        val password = "komang234354"

        coEvery { dataSource.doRegister(any(), any(), any()) } returns true
        runTest {
            repository.doRegister(fullName, email, password).map {
                delay(100)
                it
            }.test {
                delay(201)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                assertEquals(true, actualData.payload)
                coVerify { dataSource.doRegister(any(), any(), any()) }
            }
        }
    }

    @Test
    fun doRegister_loading() {
        val fullName = "Komangyuda"
        val email = "komang@@gmail.com"
        val password = "komang234354"

        coEvery { dataSource.doRegister(any(), any(), any()) } returns true
        runTest {
            repository.doRegister(fullName, email, password).map {
                delay(100)
                it
            }.test {
                delay(111)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Loading)
                coVerify { dataSource.doRegister(any(), any(), any()) }
            }
        }
    }

    @Test
    fun doRegister_error() {
        val fullName = "Komangyuda"
        val email = "komang@@gmail.com"
        val password = "komang234354"

        coEvery { dataSource.doRegister(any(), any(), any()) } throws IOException("Register failed")
        runTest {
            repository.doRegister(fullName, email, password).map {
                delay(100)
                it
            }.test {
                delay(201)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Error)
                coVerify { dataSource.doRegister(any(), any(), any()) }
            }
        }
    }

    @Test
    fun updateProfile_success() {
        val fullName = "komangyuda"

        coEvery { dataSource.updateProfile(any()) } returns true
        runTest {
            repository.updateProfile(fullName).map {
                delay(100)
                it
            }.test {
                delay(201)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                assertEquals(true, actualData.payload)
                coVerify { dataSource.updateProfile(any()) }
            }
        }
    }

    @Test
    fun updateProfile_loading() {
        val fullName = "komangyuda"

        coEvery { dataSource.updateProfile(any()) } returns true
        runTest {
            repository.updateProfile(fullName).map {
                delay(100)
                it
            }.test {
                delay(111)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Loading)
                coVerify { dataSource.updateProfile(any()) }
            }
        }
    }

    @Test
    fun updateProfile_error() {
        val fullName = "komangyuda"

        coEvery { dataSource.updateProfile(any()) } throws IOException("update Profile error")
        runTest {
            repository.updateProfile(fullName).map {
                delay(100)
                it
            }.test {
                delay(201)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Error)
                coVerify { dataSource.updateProfile(any()) }
            }
        }
    }

    @Test
    fun updatePassword_success() {
        val password = "komangyuda123"

        coEvery { dataSource.updatePassword(any()) } returns true
        runTest {
            repository.updatePassword(password).map {
                delay(100)
                it
            }.test {
                delay(201)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                assertEquals(true, actualData.payload)
                coVerify { dataSource.updatePassword(any()) }
            }
        }
    }

    @Test
    fun updatePassword_loading() {
        val password = "komangyuda123"

        coEvery { dataSource.updatePassword(any()) } returns true
        runTest {
            repository.updatePassword(password).map {
                delay(100)
                it
            }.test {
                delay(111)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Loading)
                coVerify { dataSource.updatePassword(any()) }
            }
        }
    }

    @Test
    fun updatePassword_error() {
        val password = "komangyuda123"

        coEvery { dataSource.updatePassword(any()) } throws IOException("Update password error")
        runTest {
            repository.updatePassword(password).map {
                delay(100)
                it
            }.test {
                delay(201)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Error)
                coVerify { dataSource.updatePassword(any()) }
            }
        }
    }

    @Test
    fun updateEmail_success() {
        val email = "komang@gmail.com"

        coEvery { dataSource.updateEmail(any()) } returns true
        runTest {
            repository.updateEmail(email).map {
                delay(100)
                it
            }.test {
                delay(201)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                assertEquals(true, actualData.payload)
                coVerify { dataSource.updateEmail(any()) }
            }
        }
    }

    @Test
    fun updateEmail_loading() {
        val email = "komang@gmail.com"

        coEvery { dataSource.updateEmail(any()) } returns true
        runTest {
            repository.updateEmail(email).map {
                delay(100)
                it
            }.test {
                delay(111)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Loading)
                coVerify { dataSource.updateEmail(any()) }
            }
        }
    }

    @Test
    fun updateEmail_error() {
        val email = "komang@gmail.com"

        coEvery { dataSource.updateEmail(any()) } throws IOException("update Email error")
        runTest {
            repository.updateEmail(email).map {
                delay(100)
                it
            }.test {
                delay(201)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Error)
                coVerify { dataSource.updateEmail(any()) }
            }
        }
    }

    @Test
    fun reqChangePasswordByEmailWithoutLogin_success() {
        val password = "komang123"

        coEvery { dataSource.reqChangePasswordByEmailWithoutLogin(any()) } returns true
        runTest {
            repository.reqChangePasswordByEmailWithoutLogin(password).map {
                delay(100)
                it
            }.test {
                delay(201)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                assertEquals(true, actualData.payload)
                coVerify { dataSource.reqChangePasswordByEmailWithoutLogin(any()) }
            }
        }
    }

    @Test
    fun reqChangePasswordByEmailWithoutLogin_loading() {
        val password = "komang123"

        coEvery { dataSource.reqChangePasswordByEmailWithoutLogin(any()) } returns true
        runTest {
            repository.reqChangePasswordByEmailWithoutLogin(password).map {
                delay(100)
                it
            }.test {
                delay(111)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Loading)
                coVerify { dataSource.reqChangePasswordByEmailWithoutLogin(any()) }
            }
        }
    }

    @Test
    fun reqChangePasswordByEmailWithoutLogin_error() {
        val password = "komang123"

        coEvery { dataSource.reqChangePasswordByEmailWithoutLogin(any()) } throws IOException("reqChangePass error")
        runTest {
            repository.reqChangePasswordByEmailWithoutLogin(password).map {
                delay(100)
                it
            }.test {
                delay(201)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Error)
                coVerify { dataSource.reqChangePasswordByEmailWithoutLogin(any()) }
            }
        }
    }

    @Test
    fun reqChangePasswordByEmail() {
        runTest {
            every { dataSource.reqChangePasswordByEmail() } returns true
            val actualResult = repository.reqChangePasswordByEmail()
            verify { dataSource.reqChangePasswordByEmail() }
            assertEquals(true, actualResult)
        }
    }

    @Test
    fun doLogout() {
        runTest {
            every { dataSource.doLogout() } returns true
            val actualResult = repository.doLogout()
            verify { dataSource.doLogout() }
            assertEquals(true, actualResult)
        }
    }

    @Test
    fun isLoggedIn() {
        runTest {
            every { dataSource.isLoggedIn() } returns true
            val actualResult = repository.isLoggedIn()
            verify { dataSource.isLoggedIn() }
            assertEquals(true, actualResult)
        }
    }

    @Test
    fun getCurrentUser() {
        runTest {
            val currentUser = mockk<User>()
            every { currentUser.id } returns "123"
            every { currentUser.fullName } returns "Komang"
            every { currentUser.email } returns "komang@gmail.com"
            every { dataSource.getCurrentUser() } returns currentUser

            val result = dataSource.getCurrentUser()
            assertEquals("123", result?.id)
            assertEquals("Komang", result?.fullName)
            assertEquals("komang@gmail.com", result?.email)
            verify { repository.getCurrentUser() }
        }
    }
}
