package com.kom.foodapp.presentation.checkout

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kom.foodapp.R
import com.kom.foodapp.data.datasource.cart.CartDataSource
import com.kom.foodapp.data.datasource.cart.CartDatabaseDataSource
import com.kom.foodapp.data.repository.CartRepository
import com.kom.foodapp.data.repository.CartRepositoryImpl
import com.kom.foodapp.data.source.local.database.AppDatabase
import com.kom.foodapp.databinding.ActivityCheckoutBinding
import com.kom.foodapp.presentation.checkout.adapter.PriceListAdapter
import com.kom.foodapp.presentation.common.adapter.CartListAdapter
import com.kom.foodapp.presentation.main.MainActivity
import com.kom.foodapp.utils.GenericViewModelFactory
import com.kom.foodapp.utils.formatToRupiah
import com.kom.foodapp.utils.proceedWhen

class CheckoutActivity : AppCompatActivity() {
    private val binding: ActivityCheckoutBinding by lazy {
        ActivityCheckoutBinding.inflate(layoutInflater)
    }

    private val viewModel: CheckoutViewModel by viewModels {
        val database = AppDatabase.getInstance(this)
        val dataSource: CartDataSource = CartDatabaseDataSource(database.cartDao())
        val cartRepository: CartRepository = CartRepositoryImpl(dataSource)
        GenericViewModelFactory.create(
            CheckoutViewModel(cartRepository)
        )
    }

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
        setActionOnSuccess()

    }

    private fun setActionOnSuccess() {
        binding.layoutButtonOrder.btnAddToCart.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.layout_dialog_order)
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window?.attributes)
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            dialog.window?.attributes = layoutParams
            val rvSummaryOrder = dialog.findViewById<RecyclerView>(R.id.rv_summary_order)
            rvSummaryOrder.adapter = priceItemAdapter
            val buttonClose = dialog.findViewById<Button>(R.id.btn_back_on_success)
            dialog.show()
            buttonClose.setOnClickListener {
                viewModel.deleteAllCarts()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                dialog.dismiss()
                finish()
            }
        }
    }

    private fun setActionListener() {
        binding.layoutHeader.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun observeData() {
        viewModel.checkoutData.observe(this) { result ->
            result.proceedWhen(
                doOnSuccess = {
                    binding.layoutState.root.isVisible = false
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = false
                    binding.layoutContent.root.isVisible = true
                    binding.layoutContent.rvItem.isVisible = true
                    binding.layoutButtonOrder.btnAddToCart.isVisible = true
                    result.payload?.let { (carts, priceItems, totalPrice) ->
                        adapter.submitData(carts)
                        binding.layoutButtonOrder.tvTotalPrice.text = totalPrice.formatToRupiah()
                        priceItemAdapter.submitData(priceItems)
                    }
                }, doOnLoading = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = true
                    binding.layoutState.tvError.isVisible = false
                    binding.layoutContent.root.isVisible = false
                    binding.layoutContent.rvItem.isVisible = false
                    binding.layoutButtonOrder.btnAddToCart.isVisible = false
                }, doOnError = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = true
                    binding.layoutState.tvError.text = result.exception?.message.orEmpty()
                    binding.layoutContent.root.isVisible = false
                    binding.layoutContent.rvItem.isVisible = false
                    binding.layoutButtonOrder.btnAddToCart.isVisible = false
                }, doOnEmpty = { data ->
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = true
                    binding.layoutState.tvError.text = getString(R.string.text_empty_cart)
                    data.payload?.let { (_, _, totalPrice) ->
                        binding.layoutButtonOrder.tvTotalPrice.text = totalPrice.formatToRupiah()
                    }
                    binding.layoutContent.root.isVisible = false
                    binding.layoutContent.rvItem.isVisible = false
                    binding.layoutButtonOrder.btnAddToCart.isVisible = false
                })
        }
    }

    private fun setList() {
        binding.layoutContent.rvItem.adapter = adapter
        binding.layoutContent.rvSummaryOrder.adapter = priceItemAdapter
    }
}