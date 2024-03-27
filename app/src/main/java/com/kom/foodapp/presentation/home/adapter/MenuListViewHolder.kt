package com.kom.foodapp.presentation.home.adapter

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.foodapp.model.Menu
import com.example.foodapps.base.ViewHolderBinder
import com.kom.foodapp.databinding.ItemMenuListBinding
import com.kom.foodapp.utils.formatToRupiah

class MenuListViewHolder(
    private val binding: ItemMenuListBinding,
    private val listener: MenuAdapter.OnItemClickedListener<Menu>
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Menu> {
    override fun bind(item: Menu) {
        item.let {
            binding.ivMenuImage.load(it.image)
            binding.tvMenuName.text = it.name
            binding.tvMenuPrice.text = it.price.formatToRupiah()
            itemView.setOnClickListener {
                listener.onItemSelected(item)
            }
        }
    }
}