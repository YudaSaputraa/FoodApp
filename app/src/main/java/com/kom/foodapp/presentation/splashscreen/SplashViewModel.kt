package com.kom.foodapp.presentation.splashscreen

import androidx.lifecycle.ViewModel
import com.kom.foodapp.data.repository.UserRepository

/**
Written by Komang Yuda Saputra
Github : https://github.com/YudaSaputraa
 **/
class SplashViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun isUserLoggedIn() = userRepository.isLoggedIn()
}
