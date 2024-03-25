package com.kom.foodapp.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kom.foodapp.data.datasource.profile.ProfileDataSource
import com.kom.foodapp.data.datasource.profile.ProfileDataSourceImpl
import com.kom.foodapp.data.model.Profile

class ProfileViewModel : ViewModel() {

    private val profileDataSource: ProfileDataSource = ProfileDataSourceImpl()
    private val _profileData = MutableLiveData<Profile>()

    val profileData: LiveData<Profile>
        get() = _profileData

    fun fetchProfileData() {
        val profiles = profileDataSource.getProfileData()
        if (profiles.isNotEmpty()) {
            _profileData.value = profiles[0]
        }
    }
}