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
import com.kom.foodapp.data.datasource.authentication.AuthDataSource
import com.kom.foodapp.data.datasource.authentication.FirebaseAuthDataSource
import com.kom.foodapp.data.datasource.cart.CartDataSource
import com.kom.foodapp.data.datasource.cart.CartDatabaseDataSource
import com.kom.foodapp.data.model.Cart
import com.kom.foodapp.data.repository.CartRepository
import com.kom.foodapp.data.repository.CartRepositoryImpl
import com.kom.foodapp.data.repository.UserRepository
import com.kom.foodapp.data.repository.UserRepositoryImpl
import com.kom.foodapp.data.source.firebase.FirebaseService
import com.kom.foodapp.data.source.firebase.FirebaseServiceImpl
import com.kom.foodapp.data.source.local.database.AppDatabase
import com.kom.foodapp.databinding.FragmentCartBinding
import com.kom.foodapp.databinding.FragmentProfileBinding
import com.kom.foodapp.presentation.checkout.CheckoutActivity
import com.kom.foodapp.presentation.common.adapter.CartListAdapter
import com.kom.foodapp.presentation.common.adapter.CartListener
import com.kom.foodapp.presentation.detailmenu.DetailMenuViewModel
import com.kom.foodapp.presentation.login.LoginActivity
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
        val service: FirebaseService = FirebaseServiceImpl()
        val authDataSource: AuthDataSource = FirebaseAuthDataSource(service)
        val userRepository: UserRepository = UserRepositoryImpl(authDataSource)
        GenericViewModelFactory.create(
            CartViewModel(cartRepository, userRepository)
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
            if (!viewModel.userIsLoggedIn()) {
                navigateToLogin()
            } else {
                startActivity(Intent(requireContext(), CheckoutActivity::class.java))
            }
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(requireContext(), LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        })
    }

    private fun observeData() {
        viewModel.getAllCarts().observe(viewLifecycleOwner) { result ->
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
                }
            )
        }
    }

    private fun setList() {
        binding.rvCart.adapter = this@CartFragment.adapter
    }
}