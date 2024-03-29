package com.kom.foodapp.presentation.detailmenu

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.kom.foodapp.data.model.Menu
import com.kom.foodapp.data.repository.CartRepository
import com.kom.foodapp.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import java.lang.IllegalStateException

class DetailMenuViewModel(
    private val extras: Bundle?,
    private val cartRepository: CartRepository
) : ViewModel() {
    val menu = extras?.getParcelable<Menu>(DetailActivity.EXTRAS_DETAIL_MENU)
    val menuCountLiveData = MutableLiveData(0).apply {
        postValue(0)
    }
    val priceLiveData = MutableLiveData<Double>().apply {
        postValue(0.0)
    }

    fun incrementItem() {
        val count = (menuCountLiveData.value ?: 0) + 1
        menuCountLiveData.postValue(count)
        priceLiveData.postValue(menu?.price?.times(count) ?: 0.0)
    }

    fun decrementItem() {
        if ((menuCountLiveData.value ?: 0) > 0) {
            val count = (menuCountLiveData.value ?: 0) - 1
            menuCountLiveData.postValue(count)
            priceLiveData.postValue(menu?.price?.times(count) ?: 0.0)
        }
    }

    fun addItemToCart(): LiveData<ResultWrapper<Boolean>> {
        return menu?.let {
            val quantity = menuCountLiveData.value ?: 0
            cartRepository.createCart(it, quantity).asLiveData(Dispatchers.IO)

        } ?: liveData {
            emit(ResultWrapper.Error(IllegalStateException("Menu not found!")))
        }
    }
}

