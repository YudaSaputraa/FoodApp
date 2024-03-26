package com.kom.foodapp.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.kom.foodapp.R
import com.kom.foodapp.data.model.Category
import com.kom.foodapp.databinding.ItemCategoryBinding

class CategoryAdapter(private val itemClick: (Category) -> Unit) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    fun submitData(items: List<Category>) {
        asyncDataDiffer.submitList(items)
    }

    private val asyncDataDiffer = AsyncListDiffer<Category>(
        this, object : DiffUtil.ItemCallback<Category>() {
            override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }
    )

    class CategoryViewHolder(
        private val binding: ItemCategoryBinding,
        val itemClick: (Category) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Category) {
            binding.tvCategoryName.text = item.name
            binding.ivCategoryImage.load(item.image) {
                crossfade(true)
                error(R.mipmap.ic_launcher)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding, itemClick)
    }

    override fun getItemCount(): Int = asyncDataDiffer.currentList.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(asyncDataDiffer.currentList[position])
    }
}