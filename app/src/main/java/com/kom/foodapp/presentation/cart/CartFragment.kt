package com.kom.foodapp.presentation.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kom.foodapp.R
import com.kom.foodapp.databinding.FragmentCartBinding
import com.kom.foodapp.databinding.FragmentProfileBinding

class CartFragment : Fragment() {
    private lateinit var binding : FragmentCartBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(layoutInflater, container, false )
        return binding.root
    }

}