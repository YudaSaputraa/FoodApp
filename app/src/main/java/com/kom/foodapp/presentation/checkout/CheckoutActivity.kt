package com.kom.foodapp.presentation.checkout

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.kom.foodapp.R
import com.kom.foodapp.databinding.ActivityCheckoutBinding
import com.kom.foodapp.databinding.LayoutDialogOrderBinding
import com.kom.foodapp.presentation.checkout.adapter.PriceListAdapter
import com.kom.foodapp.presentation.common.adapter.CartListAdapter
import com.kom.foodapp.presentation.login.LoginActivity
import com.kom.foodapp.utils.formatToRupiah
import com.kom.foodapp.utils.proceedWhen
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CheckoutActivity : AppCompatActivity() {
    private val binding: ActivityCheckoutBinding by lazy {
        ActivityCheckoutBinding.inflate(layoutInflater)
    }

    private val checkoutViewModel: CheckoutViewModel by viewModel()

    private val adapter: CartListAdapter by lazy {
        CartListAdapter()
    }

    private val priceItemAdapter: PriceListAdapter by lazy {
        PriceListAdapter {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setList()
        observeData()
        setActionListener()
        checkUserLoginStatus()
    }

    private fun checkUserLoginStatus() {
        lifecycleScope.launch {
            if (!checkoutViewModel.isUserLoggedIn()) {
                navigateToLogin()
            }
        }
    }

    private fun navigateToLogin() {
        startActivity(
            Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
        )
    }

    private fun showOrderSuccessDialog() {
        val dialog = Dialog(this)
        val dialogBinding = LayoutDialogOrderBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        val currentDateAndTime =
            SimpleDateFormat.getDateTimeInstance(
                SimpleDateFormat.LONG,
                SimpleDateFormat.SHORT,
                Locale.getDefault(),
            ).format(Date())
        dialogBinding.tvDateTime.text = currentDateAndTime
        checkoutViewModel.checkoutData.value?.payload?.let { (carts, _, totalPrice) ->
            dialogBinding.tvTotalPriceSuccess.text = totalPrice.formatToRupiah()
        }
        dialogBinding.rvSummaryOrderDialog.adapter = priceItemAdapter
        dialog.show()

        dialogBinding.btnBackOnSuccess.setOnClickListener {
            checkoutViewModel.deleteAllCarts()
            dialog.dismiss()
            finish()
        }
    }

    private fun setActionListener() {
        binding.layoutHeader.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.layoutButtonOrder.btnOrder.setOnClickListener {
            checkoutProcess()
        }
    }

    private fun checkoutProcess() {
        checkoutViewModel.checkoutCart().observe(this) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    binding.layoutState.root.isVisible = false
                    binding.layoutOnEmptyDataState.root.isVisible = false
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutOnEmptyDataState.ivOnEmptyData.isVisible = false
                    binding.layoutOnEmptyDataState.tvOnEmptyData.isVisible = false
                    binding.layoutContent.root.isVisible = true
                    binding.layoutContent.rvItem.isVisible = true
                    binding.layoutButtonOrder.btnOrder.isVisible = true
                    binding.layoutButtonOrder.btnOrder.isEnabled = true
                    binding.layoutButtonOrder.btnOrder.setTextColor(resources.getColor(R.color.white))
                    checkoutViewModel.deleteAllCarts()
                    showOrderSuccessDialog()
                },
                doOnLoading = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutOnEmptyDataState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = true
                    binding.layoutOnEmptyDataState.ivOnEmptyData.isVisible = false
                    binding.layoutOnEmptyDataState.tvOnEmptyData.isVisible = false
                    binding.layoutContent.root.isVisible = false
                    binding.layoutContent.rvItem.isVisible = false
                    binding.layoutButtonOrder.btnOrder.isEnabled = false
                    binding.layoutButtonOrder.tvTotalPrice.text =
                        getString(R.string.text_empty_price)
                },
                doOnError = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutOnEmptyDataState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutOnEmptyDataState.ivOnEmptyData.isVisible = true
                    binding.layoutOnEmptyDataState.tvOnEmptyData.isVisible = true
                    binding.layoutOnEmptyDataState.tvOnEmptyData.text =
                        result.exception?.message.orEmpty()
                    binding.layoutContent.root.isVisible = false
                    binding.layoutContent.rvItem.isVisible = false
                    binding.layoutButtonOrder.btnOrder.isEnabled = false
                },
                doOnEmpty = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutOnEmptyDataState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutOnEmptyDataState.ivOnEmptyData.isVisible = true
                    binding.layoutOnEmptyDataState.tvOnEmptyData.isVisible = true
                    binding.layoutOnEmptyDataState.tvOnEmptyData.text =
                        getString(R.string.text_on_cart_empty)
                    binding.layoutContent.root.isVisible = false
                    binding.layoutContent.rvItem.isVisible = false
                    binding.layoutButtonOrder.btnOrder.isEnabled = false
                },
            )
        }
    }

    private fun observeData() {
        checkoutViewModel.checkoutData.observe(this) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    binding.layoutState.root.isVisible = false
                    binding.layoutOnEmptyDataState.root.isVisible = false
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutOnEmptyDataState.ivOnEmptyData.isVisible = false
                    binding.layoutOnEmptyDataState.tvOnEmptyData.isVisible = false
                    binding.layoutContent.root.isVisible = true
                    binding.layoutContent.rvItem.isVisible = true
                    binding.layoutButtonOrder.btnOrder.isVisible = true
                    binding.layoutButtonOrder.btnOrder.isEnabled = true
                    binding.layoutButtonOrder.btnOrder.setTextColor(resources.getColor(R.color.white))
                    result.payload?.let { (carts, priceItems, totalPrice) ->
                        adapter.submitData(carts)
                        binding.layoutButtonOrder.tvTotalPrice.text = totalPrice.formatToRupiah()
                        priceItemAdapter.submitData(priceItems)
                    }
                },
                doOnLoading = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutOnEmptyDataState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = true
                    binding.layoutOnEmptyDataState.ivOnEmptyData.isVisible = false
                    binding.layoutOnEmptyDataState.tvOnEmptyData.isVisible = false
                    binding.layoutContent.root.isVisible = false
                    binding.layoutContent.rvItem.isVisible = false
                    binding.layoutButtonOrder.btnOrder.isEnabled = false
                    binding.layoutButtonOrder.tvTotalPrice.text =
                        getString(R.string.text_empty_price)
                },
                doOnError = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutOnEmptyDataState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutOnEmptyDataState.ivOnEmptyData.isVisible = true
                    binding.layoutOnEmptyDataState.tvOnEmptyData.isVisible = true
                    binding.layoutOnEmptyDataState.tvOnEmptyData.text =
                        result.exception?.message.orEmpty()
                    binding.layoutContent.root.isVisible = false
                    binding.layoutContent.rvItem.isVisible = false
                    binding.layoutButtonOrder.btnOrder.isEnabled = false
                },
                doOnEmpty = { data ->
                    binding.layoutState.root.isVisible = true
                    binding.layoutOnEmptyDataState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutOnEmptyDataState.ivOnEmptyData.isVisible = true
                    binding.layoutOnEmptyDataState.tvOnEmptyData.isVisible = true
                    binding.layoutOnEmptyDataState.tvOnEmptyData.text =
                        getString(R.string.text_on_cart_empty)
                    data.payload?.let { (_, _, totalPrice) ->
                        binding.layoutButtonOrder.tvTotalPrice.text = totalPrice.formatToRupiah()
                    }
                    binding.layoutContent.root.isVisible = true
                    binding.layoutContent.rvItem.isVisible = true
                    binding.layoutButtonOrder.btnOrder.isEnabled = false
                },
            )
        }
    }

    private fun setList() {
        binding.layoutContent.rvItem.adapter = adapter
        binding.layoutContent.rvSummaryOrder.adapter = priceItemAdapter
    }
}
