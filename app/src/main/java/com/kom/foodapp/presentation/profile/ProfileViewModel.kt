package com.kom.foodapp.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kom.foodapp.data.datasource.authentication.FirebaseAuthDataSource
import com.kom.foodapp.data.datasource.profile.ProfileDataSource
import com.kom.foodapp.data.datasource.profile.ProfileDataSourceImpl
import com.kom.foodapp.data.model.Profile
import com.kom.foodapp.data.repository.UserRepository
import com.kom.foodapp.data.repository.UserRepositoryImpl
import com.kom.foodapp.data.source.firebase.FirebaseServiceImpl
import kotlinx.coroutines.Dispatchers

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val profileDataSource: ProfileDataSource = ProfileDataSourceImpl()
    private val _profileData = MutableLiveData<Profile>()

    val profileData: LiveData<Profile>
        get() = _profileData

    val isEditProfile = MutableLiveData(false)

    constructor() : this(UserRepositoryImpl(FirebaseAuthDataSource(FirebaseServiceImpl())))

    fun changeEditMode() {
        val currentValue = isEditProfile.value ?: false
        isEditProfile.postValue(!currentValue)
    }

    fun fetchProfileData() {
        val profiles = profileDataSource.getProfileData()
        if (profiles.isNotEmpty()) {
            _profileData.value = profiles[0]
        }
    }

    fun isUserLoggedOut() = userRepository.doLogout()
    fun getCurrentUser() = userRepository.getCurrentUser()
    fun requestChangePasswordByEmail() = userRepository.reqChangePasswordByEmail()
    fun updateProfile(newFullName: String? = null) =
        userRepository
            .updateProfile(newFullName)
            .asLiveData(Dispatchers.IO)

}