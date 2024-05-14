package com.kom.foodapp.data.datasource.category

import com.kom.foodapp.data.source.network.model.category.CategoryResponse
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
class CategoryApiDataSourceTest {
    @MockK
    lateinit var service: FoodAppApiService
    private lateinit var dataSource: CategoryDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dataSource = CategoryApiDataSource(service)
    }

    @Test
    fun getCategories() {
        runTest {
            val mockResponse = mockk<CategoryResponse>(relaxed = true)
            coEvery { service.getCategories() } returns mockResponse
            val actualResult = dataSource.getCategoryData()
            coVerify { service.getCategories() }
            assertEquals(mockResponse, actualResult)
        }
    }
}
