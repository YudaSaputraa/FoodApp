package com.kom.foodapp.data.repository

import com.kom.foodapp.data.datasource.user.UserPrefDataSource

/**
Written by Komang Yuda Saputra
Github : https://github.com/YudaSaputraa
 **/
interface UserPrefRepository {
    fun isUsingGridMode(): Boolean

    fun setUsingGridMode(isUsingGridMode: Boolean)
}

class UserPrefRepositoryImpl(private val dataSource: UserPrefDataSource) : UserPrefRepository {
    override fun isUsingGridMode(): Boolean {
        return dataSource.isUsingGridMode()
    }

    override fun setUsingGridMode(isUsingGridMode: Boolean) {
        return dataSource.setUsingGridMode(isUsingGridMode)
    }
}
