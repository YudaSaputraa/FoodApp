package com.kom.foodapp.presentation.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kom.foodapp.data.repository.CartRepository
import com.kom.foodapp.data.repository.MenuRepository
import com.kom.foodapp.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CheckoutViewModel(
    private val cartRepository: CartRepository,
    private val userRepository: UserRepository,
    private val menuRepository: MenuRepository
) : ViewModel() {

    val checkoutData = cartRepository.getCheckoutData().asLiveData(Dispatchers.IO)

    fun deleteAllCarts() {
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.deleteAllCarts().collect()
        }
    }

    fun checkoutCart() = menuRepository.createOrder(
        checkoutData.value?.payload?.first.orEmpty()
    ).asLiveData(Dispatchers.IO)

    fun isUserLoggedIn() = userRepository.isLoggedIn()

}