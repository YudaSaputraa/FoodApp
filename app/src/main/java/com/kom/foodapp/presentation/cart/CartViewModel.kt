package com.kom.foodapp.presentation.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kom.foodapp.data.model.Cart
import com.kom.foodapp.data.repository.CartRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CartViewModel(private val cartRepository: CartRepository) : ViewModel() {
    fun getAllCarts() = cartRepository.getUserCartData().asLiveData(Dispatchers.IO)

    fun decreaseCart(item: Cart) {
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.decreaseCartItem(item).collect()
        }
    }

    fun increaseCart(item: Cart) {
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.increaseCartItem(item).collect()
        }
    }

    fun removeCart(item: Cart) {
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.deleteCart(item).collect()
        }
    }

    fun setCartNotes(item: Cart) {
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.setCartNotes(item).collect {
                Log.d("Set Notes", "setCartNotes: $it")
            }
        }
    }
}