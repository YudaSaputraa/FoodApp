package com.kom.foodapp.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kom.foodapp.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers

/**
Written by Komang Yuda Saputra
Github : https://github.com/YudaSaputraa
 **/
class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    fun doRegister(
        fullName: String,
        email: String,
        password: String,
    ) = repository
        .doRegister(fullName, email, password)
        .asLiveData(Dispatchers.IO)
}
