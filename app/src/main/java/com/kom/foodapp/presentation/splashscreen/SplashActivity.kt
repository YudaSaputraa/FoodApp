package com.kom.foodapp.presentation.splashscreen

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.kom.foodapp.data.datasource.authentication.AuthDataSource
import com.kom.foodapp.data.datasource.authentication.FirebaseAuthDataSource
import com.kom.foodapp.data.repository.UserRepository
import com.kom.foodapp.data.repository.UserRepositoryImpl
import com.kom.foodapp.data.source.firebase.FirebaseService
import com.kom.foodapp.data.source.firebase.FirebaseServiceImpl
import com.kom.foodapp.databinding.ActivitySplashBinding
import com.kom.foodapp.presentation.login.LoginActivity
import com.kom.foodapp.presentation.main.MainActivity
import com.kom.foodapp.utils.GenericViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    private val viewModel: SplashViewModel by viewModels {
        val service: FirebaseService = FirebaseServiceImpl()
        val authDataSource: AuthDataSource = FirebaseAuthDataSource(service)
        val userRepository: UserRepository = UserRepositoryImpl(authDataSource)
        GenericViewModelFactory.create(SplashViewModel(userRepository))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        checkUserLoginStatus()
    }

    private fun checkUserLoginStatus() {
        lifecycleScope.launch {
            delay(2000)
            if (viewModel.isUserLoggedIn()) {
                navigateToMain()
            } else {
                navigateToMain()
            }
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }
}