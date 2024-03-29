package com.kom.foodapp.presentation.cart

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.kom.foodapp.R
import com.kom.foodapp.data.datasource.cart.CartDataSource
import com.kom.foodapp.data.datasource.cart.CartDatabaseDataSource
import com.kom.foodapp.data.model.Cart
import com.kom.foodapp.data.repository.CartRepository
import com.kom.foodapp.data.repository.CartRepositoryImpl
import com.kom.foodapp.data.source.local.database.AppDatabase
import com.kom.foodapp.databinding.FragmentCartBinding
import com.kom.foodapp.databinding.FragmentProfileBinding
import com.kom.foodapp.presentation.checkout.CheckoutActivity
import com.kom.foodapp.presentation.common.adapter.CartListAdapter
import com.kom.foodapp.presentation.common.adapter.CartListener
import com.kom.foodapp.presentation.detailmenu.DetailMenuViewModel
import com.kom.foodapp.utils.GenericViewModelFactory
import com.kom.foodapp.utils.formatToRupiah
import com.kom.foodapp.utils.hideKeyboard
import com.kom.foodapp.utils.proceedWhen

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private val viewModel: CartViewModel by viewModels {
        val database = AppDatabase.getInstance(requireContext())
        val dataSource: CartDataSource = CartDatabaseDataSource(database.cartDao())
        val cartRepository: CartRepository = CartRepositoryImpl(dataSource)
        GenericViewModelFactory.create(
            CartViewModel(cartRepository)
        )
    }

    private val adapter: CartListAdapter by lazy {
        CartListAdapter(object : CartListener {
            override fun onPlusTotalItemCartClicked(cart: Cart) {
                viewModel.increaseCart(cart)
            }

            override fun onMinusTotalItemCartClicked(cart: Cart) {
                viewModel.decreaseCart(cart)
            }

            override fun onRemoveCartClicked(cart: Cart) {
                viewModel.removeCart(cart)
            }

            override fun onUserDoneEditingNotes(cart: Cart) {
                viewModel.setCartNotes(cart)
                hideKeyboard()
            }

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        tryNavigateCheckout()
        setList()
        observeData()
        setClickAction()
    }

    private fun setClickAction() {
        binding.layoutBtnTotal.btnOrder.setOnClickListener {
            startActivity(Intent(requireContext(), CheckoutActivity::class.java))
        }
    }

    private fun observeData() {
        viewModel.getAllCarts().observe(viewLifecycleOwner) { result ->
            result.proceedWhen(
                doOnLoading = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = true
                    binding.layoutState.tvError.isVisible = false
                    binding.layoutBtnTotal.btnOrder.isEnabled = false
                    binding.rvCart.isVisible = false
                },
                doOnError = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = true
                    binding.layoutBtnTotal.btnOrder.isEnabled = false
                    binding.layoutState.tvError.text = result.exception?.message.orEmpty()
                    binding.rvCart.isVisible = false
                },
                doOnEmpty = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = true
                    binding.layoutState.tvError.text = getString(R.string.text_empty_cart)
                    binding.rvCart.isVisible = false
                    binding.layoutBtnTotal.btnOrder.isEnabled = false
                    result.payload?.let { (carts, totalPrice) ->
                        binding.layoutBtnTotal.tvTotalPrice.text = totalPrice.formatToRupiah()
                    }
                },
                doOnSuccess = {
                    binding.layoutState.root.isVisible = false
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = false
                    binding.layoutState.tvError.text = getString(R.string.text_empty_cart)
                    binding.rvCart.isVisible = true
                    binding.layoutBtnTotal.btnOrder.isEnabled = true
                    result.payload?.let { (carts, totalPrice) ->
                        adapter.submitData(carts)
                        binding.layoutBtnTotal.tvTotalPrice.text = totalPrice.formatToRupiah()
                    }
                }
            )
        }
    }

    private fun setList() {
        binding.rvCart.adapter = this@CartFragment.adapter
    }
}