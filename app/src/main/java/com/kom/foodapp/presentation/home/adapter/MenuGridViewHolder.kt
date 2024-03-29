package com.kom.foodapp.presentation.home.adapter

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.kom.foodapp.base.ViewHolderBinder
import com.kom.foodapp.data.model.Menu
import com.kom.foodapp.databinding.ItemMenuGridBinding
import com.kom.foodapp.utils.formatToRupiah

class MenuGridViewHolder(
    private val binding: ItemMenuGridBinding,
    private val listener: MenuAdapter.OnItemClickedListener<Menu>
) : ViewHolder(binding.root), ViewHolderBinder<Menu> {
    override fun bind(item: Menu) {
        item.let {
            binding.ivMenuImage.load(it.imageUrl)
            binding.tvMenuName.text = it.name
            binding.tvMenuPrice.text = it.price.formatToRupiah()
            itemView.setOnClickListener{
                listener.onItemSelected(item)
            }
        }
    }
}