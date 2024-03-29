package com.kom.foodapp.presentation.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.kom.foodapp.R
import com.kom.foodapp.base.ViewHolderBinder
import com.kom.foodapp.data.model.Cart
import com.kom.foodapp.databinding.ItemCartBinding
import com.kom.foodapp.databinding.ItemCheckoutMenuBinding
import com.kom.foodapp.utils.doneEditing
import com.kom.foodapp.utils.formatToRupiah

class CartListAdapter(private val cartListener: CartListener? = null) :
    RecyclerView.Adapter<ViewHolder>() {

    private val dataDiffer =
        AsyncListDiffer(this, object : DiffUtil.ItemCallback<Cart>() {
            override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

        })

    fun submitData(data: List<Cart>) {
        dataDiffer.submitList(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (cartListener != null) CartViewHolder(
            ItemCartBinding.inflate(
                LayoutInflater.from(parent.context), parent, false

            ), cartListener
        ) else CartOrderViewHolder(
            ItemCheckoutMenuBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as ViewHolderBinder<Cart>).bind(dataDiffer.currentList[position])
    }
}

class CartViewHolder(
    private val binding: ItemCartBinding,
    private val cartListener: CartListener?
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Cart> {
    override fun bind(item: Cart) {
        setCartData(item)
        setCartNotes(item)
        setClickListener(item)
    }

    private fun setClickListener(item: Cart) {
        with(binding) {
            layoutItemQuantity.ivMinus.setOnClickListener { cartListener?.onMinusTotalItemCartClicked(item) }
            layoutItemQuantity.ivPlus.setOnClickListener { cartListener?.onPlusTotalItemCartClicked(item) }
            ivTrash.setOnClickListener { cartListener?.onRemoveCartClicked(item) }
        }
    }

    private fun setCartNotes(item: Cart) {
        binding.etTextNote.setText(item.itemNotes)
        binding.etTextNote.doneEditing {
            binding.etTextNote.clearFocus()
            val newItem = item.copy().apply {
                itemNotes = binding.etTextNote.text.toString().trim()
            }
            cartListener?.onUserDoneEditingNotes(newItem)

        }

    }

    private fun setCartData(item: Cart) {
        with(binding) {
            binding.ivMenuImg.load(item.menuImgUrl) {
                crossfade(true)
            }
            layoutItemQuantity.tvQuantity.text = item.itemQuantity.toString()
            tvCheckoutMenuName.text = item.menuName
            tvCheckoutMenuPrice.text = item.menuPrice.formatToRupiah()

        }
    }

}

class CartOrderViewHolder(
    private val binding: ItemCheckoutMenuBinding,
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Cart> {
    override fun bind(item: Cart) {
        setCartData(item)
        setCartNotes(item)
    }

    private fun setCartData(item: Cart) {
        with(binding) {
            binding.ivMenuImg.load(item.menuImgUrl) {
                crossfade(true)
            }
            tvTotalQuantity.text =
                itemView.rootView.context.getString(
                    R.string.total_quantity,
                    item.itemQuantity.toString()
                )
            tvCheckoutMenuName.text = item.menuName
            tvCheckoutMenuPrice.text = item.menuPrice.formatToRupiah()
        }
    }

    private fun setCartNotes(item: Cart) {
        binding.tvTextNote.text = item.itemNotes
    }

}

interface CartListener {
    fun onPlusTotalItemCartClicked(cart: Cart)
    fun onMinusTotalItemCartClicked(cart: Cart)
    fun onRemoveCartClicked(cart: Cart)
    fun onUserDoneEditingNotes(cart: Cart)
}