package com.kom.foodapp.presentation.detailmenu

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.example.foodapp.model.Menu
import com.kom.foodapp.R
import com.kom.foodapp.data.datasource.cart.CartDataSource
import com.kom.foodapp.data.datasource.cart.CartDatabaseDataSource
import com.kom.foodapp.data.repository.CartRepository
import com.kom.foodapp.data.repository.CartRepositoryImpl
import com.kom.foodapp.data.source.local.database.AppDatabase
import com.kom.foodapp.databinding.ActivityDetailBinding
import com.kom.foodapp.utils.GenericViewModelFactory
import com.kom.foodapp.utils.formatToRupiah
import com.kom.foodapp.utils.proceedWhen

class DetailActivity : AppCompatActivity() {
    private val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    private val viewModel: DetailMenuViewModel by viewModels {
        val database = AppDatabase.getInstance(this)
        val dataSource: CartDataSource = CartDatabaseDataSource(database.cartDao())
        val cartRepository: CartRepository = CartRepositoryImpl(dataSource)
        GenericViewModelFactory.create(
            DetailMenuViewModel(intent?.extras, cartRepository)
        )
    }

    companion object {
        const val EXTRAS_DETAIL_MENU = "EXTRAS_DETAIL_MENU"

        fun startActivity(context: Context, menu: Menu) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(EXTRAS_DETAIL_MENU, menu)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        bindMenu(viewModel.menu)
        navigateToGoogleMaps(viewModel.menu)
        observeData()
        setClickListener()

    }

    private fun setClickListener() {
        binding.layoutDetailMenu.icBack.setOnClickListener {
            onBackPressed()
        }
        binding.layoutAddCart.ivMinus.setOnClickListener {
            viewModel.decrementItem()
        }
        binding.layoutAddCart.ivPlus.setOnClickListener {
            viewModel.incrementItem()
        }
        binding.layoutAddCart.btnAddToCart.setOnClickListener {
            addMenuToCart()
        }
    }

    private fun addMenuToCart() {
        viewModel.addItemToCart().observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    Toast.makeText(
                        this,
                        getString(R.string.text_add_cart_on_success), Toast.LENGTH_SHORT
                    ).show()
                    finish()
                },
                doOnError = {
                    Toast.makeText(
                        this,
                        getString(R.string.text_add_cart_on_error), Toast.LENGTH_SHORT
                    ).show()
                },
                doOnLoading = {
                    Toast.makeText(
                        this,
                        getString(R.string.text_add_cart_on_loading), Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }

    private fun observeData() {
        viewModel.priceLiveData.observe(this) {
            binding.layoutAddCart.btnAddToCart.isEnabled = it != 0.0
            binding.layoutAddCart.btnAddToCart.text = getString(
                R.string.placeholder_total_price, it.formatToRupiah()
            )
        }
        viewModel.menuCountLiveData.observe(this) {
            binding.layoutAddCart.tvQuantity.text = it.toString()
        }
    }

    private fun bindMenu(menu: Menu?) {
        menu?.let { item ->
            binding.layoutDetailMenu.ivDetailMenu.load(item.imageUrl) {
                crossfade(true)

            }
            binding.layoutDetailMenu.tvDetailMenuName.text = menu.name
            binding.layoutDetailMenu.tvDetailMenuDesc.text = menu.desc
            binding.layoutDetailMenu.tvDetailMenuPrice.text = menu.price.formatToRupiah()
            binding.layoutDetailLocation.tvDetailLocationAddress.text = menu.locationAddress

        }
    }

    private fun navigateToGoogleMaps(menu: Menu?) {
        menu?.let { item->
        binding.layoutDetailLocation.tvDetailLocationAddress.setOnClickListener {
            openGoogleMaps(item.locationUrl)
        }
        }
    }

    private fun openGoogleMaps(it: String) {
        val gmmIntentUri = Uri.parse(it)
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        startActivity(mapIntent)
    }

}