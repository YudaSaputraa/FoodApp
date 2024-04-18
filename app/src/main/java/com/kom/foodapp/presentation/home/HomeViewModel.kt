package com.kom.foodapp.presentation.home

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kom.foodapp.data.model.Menu
import com.kom.foodapp.data.repository.CartRepository
import com.kom.foodapp.data.repository.CategoryRepository
import com.kom.foodapp.data.repository.MenuRepository
import com.kom.foodapp.utils.proceedWhen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val categoryRepository: CategoryRepository,
    private val menuRepository: MenuRepository,
    private val cartRepository: CartRepository
) : ViewModel() {
    val menuCountLiveData = MutableLiveData(0).apply {
        postValue(0)
    }

    fun getMenu(categoryName: String? = null) =
        menuRepository.getMenu(categoryName).asLiveData(Dispatchers.IO)

    fun getCategories() = categoryRepository.getCategories().asLiveData(Dispatchers.IO)

    fun addItemToCart(menu: Menu) {
        menuCountLiveData.value = 1

        viewModelScope.launch {
            cartRepository.createCart(menu, 1).collect{
                it.proceedWhen(
                    doOnSuccess = {
                        Log.d(TAG, "addItemToCart: Success!")
                    },
                    doOnError = {
                        Log.d(TAG, "addItemToCart: Failed!")
                    },
                    doOnEmpty = {
                        Log.d(TAG, "addItemToCart: Empty!")
                    }
                )
            }
        }
    }
}