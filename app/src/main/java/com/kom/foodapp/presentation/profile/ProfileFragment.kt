package com.kom.foodapp.presentation.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import coil.load
import com.kom.foodapp.R
import com.kom.foodapp.data.datasource.authentication.AuthDataSource
import com.kom.foodapp.data.datasource.authentication.FirebaseAuthDataSource
import com.kom.foodapp.data.repository.UserRepository
import com.kom.foodapp.data.repository.UserRepositoryImpl
import com.kom.foodapp.data.source.firebase.FirebaseService
import com.kom.foodapp.data.source.firebase.FirebaseServiceImpl
import com.kom.foodapp.databinding.FragmentProfileBinding
import com.kom.foodapp.presentation.login.LoginActivity
import com.kom.foodapp.utils.GenericViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()

    private val viewModel: ProfileViewModel by viewModels {
        val service: FirebaseService = FirebaseServiceImpl()
        val authDataSource: AuthDataSource = FirebaseAuthDataSource(service)
        val userRepository: UserRepository = UserRepositoryImpl(authDataSource)
        GenericViewModelFactory.create(ProfileViewModel(userRepository))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editProfile()
        setClickListener()
        loggedOut()
        checkUserLoginStatus()
        profileViewModel.fetchProfileData()
        observeProfileData()

    }

    private fun checkUserLoginStatus() {
        lifecycleScope.launch {
            if (!viewModel.isUserLoggedIn()) {
                navigateToLogin()
            }
        }
    }

    private fun observeProfileData() {
        profileViewModel.profileData.observe(viewLifecycleOwner, Observer { profile ->
            binding.layoutProfile.ivProfile.load(profile.image) {
                crossfade(true)
                error(R.drawable.img_error)
            }
            binding.layoutProfile.editTextUsername.setText(profile.name)
            binding.layoutProfile.editTextEmail.setText(profile.email)
            binding.layoutProfile.editTextPhoneNumber.setText(profile.phoneNumber)
        })
    }

    private fun loggedOut() {
        binding.btnLogout.setOnClickListener {
            viewModel.isUserLoggedOut()
            Toast.makeText(requireContext(), "Logout Success!", Toast.LENGTH_SHORT).show()
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(requireContext(), LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        })
    }

    private fun setClickListener() {
        binding.layoutHeader.ivEditProfile.setOnClickListener {
            profileViewModel.changeEditMode()
        }
    }

    private fun editProfile() {
        profileViewModel.isEditProfile.observe(viewLifecycleOwner) {
            binding.layoutProfile.editTextUsername.isEnabled = it
            binding.layoutProfile.editTextEmail.isEnabled = it
            binding.layoutProfile.editTextPhoneNumber.isEnabled = it

        }
    }

}