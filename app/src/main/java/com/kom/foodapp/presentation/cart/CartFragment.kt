package com.kom.foodapp.presentation.cart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.kom.foodapp.R
import com.kom.foodapp.data.model.Cart
import com.kom.foodapp.databinding.FragmentCartBinding
import com.kom.foodapp.presentation.checkout.CheckoutActivity
import com.kom.foodapp.presentation.common.adapter.CartListAdapter
import com.kom.foodapp.presentation.common.adapter.CartListener
import com.kom.foodapp.presentation.login.LoginActivity
import com.kom.foodapp.utils.formatToRupiah
import com.kom.foodapp.utils.hideKeyboard
import com.kom.foodapp.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private val cartViewModel: CartViewModel by viewModel()

    private val adapter: CartListAdapter by lazy {
        CartListAdapter(
            object : CartListener {
                override fun onPlusTotalItemCartClicked(cart: Cart) {
                    cartViewModel.increaseCart(cart)
                }

                override fun onMinusTotalItemCartClicked(cart: Cart) {
                    cartViewModel.decreaseCart(cart)
                }

                override fun onRemoveCartClicked(cart: Cart) {
                    cartViewModel.removeCart(cart)
                }

                override fun onUserDoneEditingNotes(cart: Cart) {
                    cartViewModel.setCartNotes(cart)
                    hideKeyboard()
                }
            },
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCartBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setList()
        observeData()
        setClickListener()
    }

    private fun setClickListener() {
        binding.layoutBtnTotal.btnOrder.setOnClickListener {
            if (!cartViewModel.userIsLoggedIn()) {
                navigateToLogin()
            } else {
                startActivity(Intent(requireContext(), CheckoutActivity::class.java))
            }
        }
    }

    private fun navigateToLogin() {
        startActivity(
            Intent(requireContext(), LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
        )
    }

    private fun observeData() {
        cartViewModel.getAllCarts().observe(viewLifecycleOwner) { result ->
            result.proceedWhen(
                doOnLoading = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutOnEmptyDataState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = true
                    binding.layoutOnEmptyDataState.ivOnEmptyData.isVisible = false
                    binding.layoutOnEmptyDataState.tvOnEmptyData.isVisible = false
                    binding.layoutBtnTotal.btnOrder.isEnabled = false
                    binding.rvCart.isVisible = false
                    binding.layoutBtnTotal.tvTotalPrice.text = getString(R.string.text_empty_price)
                },
                doOnError = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutOnEmptyDataState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutBtnTotal.btnOrder.isEnabled = false
                    binding.layoutOnEmptyDataState.tvOnEmptyData.isVisible = true
                    binding.layoutOnEmptyDataState.ivOnEmptyData.isVisible = true
                    binding.layoutOnEmptyDataState.tvOnEmptyData.text =
                        result.exception?.message.orEmpty()

                    binding.rvCart.isVisible = false
                },
                doOnEmpty = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutOnEmptyDataState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutOnEmptyDataState.ivOnEmptyData.isVisible = true
                    binding.layoutOnEmptyDataState.tvOnEmptyData.isVisible = true
                    binding.layoutOnEmptyDataState.tvOnEmptyData.text =
                        getString(R.string.text_on_cart_empty)
                    binding.rvCart.isVisible = false
                    binding.layoutBtnTotal.btnOrder.isEnabled = false
                    result.payload?.let { (carts, totalPrice) ->
                        binding.layoutBtnTotal.tvTotalPrice.text = totalPrice.formatToRupiah()
                    }
                },
                doOnSuccess = {
                    binding.layoutState.root.isVisible = false
                    binding.layoutOnEmptyDataState.root.isVisible = false
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutOnEmptyDataState.ivOnEmptyData.isVisible = false
                    binding.layoutOnEmptyDataState.tvOnEmptyData.isVisible = false
                    binding.rvCart.isVisible = true
                    binding.layoutBtnTotal.btnOrder.isEnabled = true
                    binding.layoutBtnTotal.btnOrder.setTextColor(resources.getColor(R.color.white))
                    result.payload?.let { (carts, totalPrice) ->
                        adapter.submitData(carts)
                        binding.layoutBtnTotal.tvTotalPrice.text = totalPrice.formatToRupiah()
                    }
                },
            )
        }
    }

    private fun setList() {
        binding.rvCart.adapter = this@CartFragment.adapter
    }
}
