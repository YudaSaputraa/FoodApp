package com.kom.foodapp.presentation.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kom.foodapp.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers

class ProfileViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    val isEditProfile = MutableLiveData(false)

    fun changeEditMode() {
        val currentValue = isEditProfile.value ?: false
        isEditProfile.postValue(!currentValue)
    }

    fun isUserLoggedOut() = userRepository.doLogout()

    fun getCurrentUser() = userRepository.getCurrentUser()

    fun requestChangePasswordByEmail() = userRepository.reqChangePasswordByEmail()

    fun updateProfile(newFullName: String? = null) =
        userRepository
            .updateProfile(newFullName)
            .asLiveData(Dispatchers.IO)
}
