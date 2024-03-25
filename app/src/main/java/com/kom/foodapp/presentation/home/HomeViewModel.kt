package com.kom.foodapp.presentation.home

import androidx.lifecycle.ViewModel
import com.kom.foodapp.data.repository.CategoryRepository
import com.kom.foodapp.data.repository.MenuRepository

class HomeViewModel(
    private val categoryRepository: CategoryRepository,
    private val menuRepository: MenuRepository
) : ViewModel() {
    fun getMenu() = menuRepository.getMenu()
    fun getCategories() = categoryRepository.getCategories()

}