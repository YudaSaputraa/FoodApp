package com.kom.foodapp.presentation.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import coil.load
import com.kom.foodapp.R
import com.kom.foodapp.databinding.FragmentHomeBinding
import com.kom.foodapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel.fetchProfileData()

        profileViewModel.profileData.observe(viewLifecycleOwner, Observer { profile ->
            binding.layoutProfile.ivProfile.load(profile.image) {
                crossfade(true)
                error(R.mipmap.ic_launcher)
            }
            binding.layoutProfile.editTextUsername.setText(profile.name)
            binding.layoutProfile.editTextEmail.setText(profile.email)
            binding.layoutProfile.editTextPhoneNumber.setText(profile.phoneNumber)
        })
    }

}