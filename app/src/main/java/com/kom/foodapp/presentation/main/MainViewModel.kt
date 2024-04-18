package com.kom.foodapp.presentation.main

import androidx.lifecycle.ViewModel
import com.kom.foodapp.data.repository.UserRepository

/**
Written by Komang Yuda Saputra
Github : https://github.com/YudaSaputraa
 **/
class MainViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun isUserLoggedIn() = userRepository.isLoggedIn()
}