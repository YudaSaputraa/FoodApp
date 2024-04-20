package com.kom.foodapp.presentation.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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
import com.kom.foodapp.utils.proceedWhen

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
        profileViewModel.fetchProfileData()
        observeProfileData()
        reqChangePasswordByEmail()
    }

    private fun reqChangePasswordByEmail() {
        binding.btnChangePassword.setOnClickListener {
            confirmChangePassword()
        }
    }

    private fun confirmChangeProfileData() {
        val dialog = AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.text_confirm_edit_profile_data))
            .setPositiveButton(
                "Ya"
            ) { dialog, id ->
                doChangeFullName()
            }
            .setNegativeButton(
                "Tidak"
            ) { dialog, id ->
            }.create()
        dialog.show()
    }

    private fun confirmChangePassword() {
        val dialog = AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.text_confirm_change_password_dialog))
            .setPositiveButton(
                "Ya"
            ) { dialog, id ->
                viewModel.requestChangePasswordByEmail()
                requestChangePasswordDialog()
            }
            .setNegativeButton(
                "Tidak"
            ) { dialog, id ->
            }.create()
        dialog.show()
    }

    private fun requestChangePasswordDialog() {
        val dialog = AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.text_dialog_when_change_password))
            .setPositiveButton(
                getString(R.string.text_positive_button_dialog)
            ) { dialog, id ->


            }.create()
        dialog.show()
    }

    private fun observeProfileData() {
        val currentUser = viewModel.getCurrentUser()
        profileViewModel.profileData.observe(viewLifecycleOwner, Observer { profile ->
            binding.layoutProfile.ivProfile.load(profile.image) {
                crossfade(true)
                error(R.drawable.img_error)
            }
            binding.layoutProfile.etFullName.setText(currentUser?.fullName)
            binding.layoutProfile.etEmail.setText(currentUser?.email)
        })
    }

    private fun loggedOut() {
        binding.btnLogout.setOnClickListener {
            viewModel.isUserLoggedOut()
            Toast.makeText(requireContext(), "Logout Success!", Toast.LENGTH_SHORT).show()
            navigateToLogin()
            requireActivity().supportFragmentManager.popBackStack(
                null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )

        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(requireContext(), LoginActivity::class.java).apply {
        })
    }

    private fun setClickListener() {
        binding.layoutHeader.ivEditProfile.setOnClickListener {
            profileViewModel.changeEditMode()
            profileViewModel.isEditProfile.observe(viewLifecycleOwner) { isEditMode ->
                binding.btnSave.isVisible = isEditMode
            }
        }
        binding.btnSave.setOnClickListener {
            if (isFormFullNameValid())
                confirmChangeProfileData()
        }
    }

    private fun doChangeFullName() {
        if (isFormFullNameValid()) {
            val newFullName = binding.layoutProfile.etFullName.text.toString().trim()
            changeProfileProcess(newFullName)
        }
    }

    private fun isFormFullNameValid(): Boolean {
        val newFullName = binding.layoutProfile.etFullName.text.toString().trim()
        return changeFullNameFormValidation(newFullName)
    }

    private fun changeFullNameFormValidation(newFullName: String): Boolean {
        val currentFullName = viewModel.getCurrentUser()
        return if (newFullName == currentFullName?.fullName) {
            binding.layoutProfile.tilFullName.isErrorEnabled = true
            binding.layoutProfile.tilFullName.error =
                getString(R.string.text_error_when_same_fullname)
            false
        } else if (newFullName.isEmpty()) {
            binding.layoutProfile.tilFullName.isErrorEnabled = true
            binding.layoutProfile.tilFullName.error =
                getString(R.string.text_fullname_cannot_empty)
            false
        } else {
            binding.layoutProfile.tilFullName.isErrorEnabled = false
            true
        }
    }


    private fun changeProfileProcess(newFullName: String) {
        viewModel.updateProfile(newFullName).observe(viewLifecycleOwner) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    binding.btnSave.isVisible = false
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.text_success_edit_profile), Toast.LENGTH_SHORT
                    ).show()
                    profileViewModel.changeEditMode()
                },
                doOnError = {
                    Toast.makeText(
                        requireContext(),
                        "error ${it.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }

    private fun editProfile() {
        profileViewModel.isEditProfile.observe(viewLifecycleOwner) {
            binding.layoutProfile.etFullName.isEnabled = it

        }
    }

}