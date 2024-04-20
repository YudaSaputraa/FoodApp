package com.kom.foodapp.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.kom.foodapp.R
import com.kom.foodapp.data.datasource.authentication.AuthDataSource
import com.kom.foodapp.data.datasource.authentication.FirebaseAuthDataSource
import com.kom.foodapp.data.repository.UserRepository
import com.kom.foodapp.data.repository.UserRepositoryImpl
import com.kom.foodapp.data.source.firebase.FirebaseService
import com.kom.foodapp.data.source.firebase.FirebaseServiceImpl
import com.kom.foodapp.databinding.ActivityMainBinding
import com.kom.foodapp.presentation.login.LoginActivity
import com.kom.foodapp.utils.GenericViewModelFactory

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: MainViewModel by viewModels {
        val service: FirebaseService = FirebaseServiceImpl()
        val authDataSource: AuthDataSource = FirebaseAuthDataSource(service)
        val userRepository: UserRepository = UserRepositoryImpl(authDataSource)
        GenericViewModelFactory.create(MainViewModel(userRepository))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setBottomNavbar()
    }

    private fun setBottomNavbar() {
        val navController = findNavController(R.id.nav_host)
        binding.navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { controller, destination, argumen ->
            when (destination.id) {
                R.id.menu_profile_tab -> {
                    if (!viewModel.isUserLoggedIn()) {
                        navigateToLogin()
                        controller.popBackStack(R.id.menu_home_tab, false)
                    }
                }
            }
        }

    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        })
    }


}