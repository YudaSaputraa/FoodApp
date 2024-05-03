package com.kom.foodapp.data.datasource.user

import com.kom.foodapp.data.source.local.pref.UserPreference

/**
Written by Komang Yuda Saputra
Github : https://github.com/YudaSaputraa
 **/
interface UserPrefDataSource {
    fun isUsingGridMode(): Boolean

    fun setUsingGridMode(isUsingGridMode: Boolean)
}

class UserPrefDataSourceImpl(private val userPreference: UserPreference) : UserPrefDataSource {
    override fun isUsingGridMode(): Boolean {
        return userPreference.isUsingGridMode()
    }

    override fun setUsingGridMode(isUsingGridMode: Boolean) {
        return userPreference.setUsingGridMode(isUsingGridMode)
    }
}
