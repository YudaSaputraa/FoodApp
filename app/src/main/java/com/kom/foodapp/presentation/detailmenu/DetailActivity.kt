package com.kom.foodapp.presentation.detailmenu

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.load
import com.example.foodapp.model.Menu
import com.kom.foodapp.R
import com.kom.foodapp.databinding.ActivityDetailBinding
import com.kom.foodapp.utils.formatToRupiah

class DetailActivity : AppCompatActivity() {
    private val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }
    private var quantity = 1
    private var totalPrice: Double = 0.0

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
        getIntentData()
        backToHome()
        intent.extras?.getParcelable<Menu>(EXTRAS_DETAIL_MENU)?.let { setItemQuantity(it) }
    }

    private fun setItemQuantity(menu: Menu) {
        binding.layoutAddCart.ivMinus.setOnClickListener {
            if (quantity > 1) {
                quantity -= 1
                totalPrice -= menu.price
                updateQuantityLayout()
            } else {
                Toast.makeText(this, getString(R.string.text_toast_on_quantity), Toast.LENGTH_SHORT)
                    .show()
            }
        }
        binding.layoutAddCart.ivPlus.setOnClickListener {
            quantity += 1
            totalPrice += menu.price
            updateQuantityLayout()
        }
        updateQuantityLayout()
    }

    private fun updateQuantityLayout() {
        binding.layoutAddCart.tvQuantity.text = quantity.toString()
        binding.layoutAddCart.btnAddToCart.text =
            getString(
                R.string.placeholder_total_price, totalPrice.formatToRupiah()
            )

    }

    private fun getIntentData() {
        intent.extras?.getParcelable<Menu>(EXTRAS_DETAIL_MENU)?.let {
            setImgMenu(it.image)
            setDetailData(it)
            setItemQuantity(it)
            totalPrice = it.price

        }
    }

    private fun navigateToGoogleMaps(menu: Menu) {
        binding.layoutDetailLocation.tvDetailLocationAddress.setOnClickListener {
            openGoogleMaps(menu.locationUrl)
        }
    }

    private fun openGoogleMaps(it: String) {
        val gmmIntentUri = Uri.parse(it)
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        startActivity(mapIntent)
    }

    private fun setDetailData(menu: Menu) {
        binding.layoutDetailMenu.tvDetailMenuName.text = menu.name
        binding.layoutDetailMenu.tvDetailMenuDesc.text = menu.desc
        binding.layoutDetailMenu.tvDetailMenuPrice.text = menu.price.formatToRupiah()
        binding.layoutDetailLocation.tvDetailLocationAddress.text = menu.locationAddress
        navigateToGoogleMaps(menu)

    }

    private fun setImgMenu(image: String?) {
        image?.let { binding.layoutDetailMenu.ivDetailMenu.load(it) }
    }

    private fun backToHome() {
        binding.layoutDetailMenu.icBack.setOnClickListener {
            finish()
        }
    }
}