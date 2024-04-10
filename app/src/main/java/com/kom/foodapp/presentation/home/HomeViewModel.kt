package com.kom.foodapp.presentation.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kom.foodapp.data.model.Menu
import com.kom.foodapp.data.repository.CartRepository
import com.kom.foodapp.data.repository.CategoryRepository
import com.kom.foodapp.data.repository.MenuRepository
import com.kom.foodapp.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers

class HomeViewModel(
    private val categoryRepository: CategoryRepository,
    private val menuRepository: MenuRepository,
    private val cartRepository: CartRepository
) : ViewModel() {
    val menuCountLiveData = MutableLiveData(0).apply {
        postValue(0)
    }
    val priceLiveData = MutableLiveData<Double>().apply {
        postValue(0.0)
    }

    fun getMenu(categoryName: String? = null) =
        menuRepository.getMenu(categoryName).asLiveData(Dispatchers.IO)

    fun getCategories() = categoryRepository.getCategories().asLiveData(Dispatchers.IO)

    fun addItemToCart(menu: Menu) {
        menuCountLiveData.value = 1

        cartRepository.createCart(menu, 1)
            .asLiveData(Dispatchers.IO)
            .observeForever { result ->
                when (result) {
                    is ResultWrapper.Success -> {
                        println("Cart berhasil dibuat")
                    }

                    is ResultWrapper.Error -> {
                        println("Terjadi kesalahan")
                    }

                    is ResultWrapper.Loading -> {
                        println("Loading...")
                    }

                    else -> {
                    }
                }
            }
    }


}