package com.kom.foodapp.data.repository

import app.cash.turbine.test
import com.kom.foodapp.data.datasource.category.CategoryDataSource
import com.kom.foodapp.data.source.network.model.category.CategoryItemResponse
import com.kom.foodapp.data.source.network.model.category.CategoryResponse
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
import java.lang.IllegalStateException

/**
 * Written by Komang Yuda Saputra
 * Github : https://github.com/YudaSaputraa
 */
class CategoryRepositoryImplTest {
    @MockK
    lateinit var dataSource: CategoryDataSource
    private lateinit var repository: CategoryRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = CategoryRepositoryImpl(dataSource)
    }

    @Test
    fun getCategories_success() {
        val category1 =
            CategoryItemResponse(
                imgUrl = "exampleImageUrl",
                name = "Makanan",
            )
        val category2 =
            CategoryItemResponse(
                imgUrl = "exampleImageUrl2",
                name = "Minuman",
            )
        val mockListCategory = listOf(category1, category2)
        val mockResponse = mockk<CategoryResponse>()
        every { mockResponse.data } returns mockListCategory
        runTest {
            coEvery { dataSource.getCategoryData() } returns mockResponse
            repository.getCategories().map {
                delay(100)
                it
            }.test {
                delay(2210)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                coVerify { dataSource.getCategoryData() }
            }
        }
    }

    @Test
    fun getCategories_loading() {
        val category1 =
            CategoryItemResponse(
                imgUrl = "exampleImageUrl",
                name = "Makanan",
            )
        val category2 =
            CategoryItemResponse(
                imgUrl = "exampleImageUrl2",
                name = "Minuman",
            )
        val mockListCategory = listOf(category1, category2)
        val mockResponse = mockk<CategoryResponse>()
        every { mockResponse.data } returns mockListCategory
        runTest {
            coEvery { dataSource.getCategoryData() } returns mockResponse
            repository.getCategories().map {
                delay(100)
                it
            }.test {
                delay(110)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Loading)
                coVerify { dataSource.getCategoryData() }
            }
        }
    }

    @Test
    fun getCategories_error() {
        runTest {
            coEvery { dataSource.getCategoryData() } throws IllegalStateException("Mock Error")
            repository.getCategories().map {
                delay(100)
                it
            }.test {
                delay(210)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Error)
                coVerify { dataSource.getCategoryData() }
            }
        }
    }

    @Test
    fun getCategories_empty() {
        val mockListCategory = listOf<CategoryItemResponse>()
        val mockResponse = mockk<CategoryResponse>()
        every { mockResponse.data } returns mockListCategory
        runTest {
            coEvery { dataSource.getCategoryData() } returns mockResponse
            repository.getCategories().map {
                delay(100)
                it
            }.test {
                delay(210)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Empty)
                coVerify { dataSource.getCategoryData() }
            }
        }
    }
}
