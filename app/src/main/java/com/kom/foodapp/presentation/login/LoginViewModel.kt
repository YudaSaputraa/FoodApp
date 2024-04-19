package com.kom.foodapp.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kom.foodapp.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers

/**
Written by Komang Yuda Saputra
Github : https://github.com/YudaSaputraa
 **/
class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    fun doLogin(email: String, password: String) =
        repository
            .doLogin(email, password)
            .asLiveData(Dispatchers.IO)

    fun doRequestResetPasswordByEmail() = repository.reqChangePasswordByEmail()
}