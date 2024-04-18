package com.kom.foodapp.data.datasource.authentication

import com.kom.foodapp.data.model.User
import com.kom.foodapp.data.model.toUser
import com.kom.foodapp.data.source.firebase.FirebaseService
import java.lang.Exception

/**
Written by Komang Yuda Saputra
Github : https://github.com/YudaSaputraa
 **/
interface AuthDataSource {

    @Throws(exceptionClasses = [Exception::class])
    suspend fun doLogin(email : String, password : String) : Boolean


    @Throws(exceptionClasses = [Exception::class])
    suspend fun doRegister(fullName : String, email : String, password : String) : Boolean

     @Throws(exceptionClasses = [Exception::class])
    suspend fun updateProfile(fullName : String? = null) : Boolean


     @Throws(exceptionClasses = [Exception::class])
    suspend fun updatePassword(newPassword : String) : Boolean

     @Throws(exceptionClasses = [Exception::class])
    suspend fun updateEmail(newEmail : String) : Boolean


    fun reqChangePasswordByEmail() : Boolean
    fun doLogout():Boolean
    fun isLoggedIn(): Boolean
    fun getCurrentUser():User?


}

class FirebaseAuthDataSource(private val service: FirebaseService) : AuthDataSource{
    override suspend fun doLogin(email: String, password: String): Boolean {
        return service.doLogin(email, password)
    }

    override suspend fun doRegister(
        fullName: String,
        email: String,
        password: String
    ): Boolean {
        return service.doRegister(fullName, email, password)
    }

    override suspend fun updateProfile(fullName: String?): Boolean {
    return service.updateProfile(fullName)
    }

    override suspend fun updatePassword(newPassword: String): Boolean {
       return service.updatePassword(newPassword)
    }

    override suspend fun updateEmail(newEmail: String): Boolean {
      return service.updateEmail(newEmail)
    }

    override fun reqChangePasswordByEmail(): Boolean {
       return service.reqChangePasswordByEmail()
    }

    override fun doLogout(): Boolean {
        return service.doLogout()
    }

    override fun isLoggedIn(): Boolean {
      return service.isLoggedIn()
    }

    override fun getCurrentUser(): User? {
      return service.getCurrentUser().toUser()
    }

}