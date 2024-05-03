package com.kom.foodapp.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kom.foodapp.R
import com.kom.foodapp.base.ViewHolderBinder
import com.kom.foodapp.data.model.Menu
import com.kom.foodapp.databinding.ItemMenuGridBinding
import com.kom.foodapp.databinding.ItemMenuListBinding

class MenuAdapter(
    var listener: OnItemClickedListener<Menu>,
    val listMode: Int = MODE_LIST,
) :
    RecyclerView.Adapter<ViewHolder>() {
    companion object {
        const val MODE_GRID = 1
        const val MODE_LIST = 0
    }

    private val asyncDataDiffer =
        AsyncListDiffer<Menu>(
            this,
            object : DiffUtil.ItemCallback<Menu>() {
                override fun areItemsTheSame(
                    oldItem: Menu,
                    newItem: Menu,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: Menu,
                    newItem: Menu,
                ): Boolean {
                    return oldItem.hashCode() == newItem.hashCode()
                }
            },
        )

    fun submitData(data: List<Menu>) {
        asyncDataDiffer.submitList(data)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        return if (listMode == MODE_GRID) {
            MenuGridViewHolder(
                ItemMenuGridBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                listener,
            )
        } else {
            MenuListViewHolder(
                ItemMenuListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                listener,
            )
        }
    }

    override fun getItemCount(): Int = asyncDataDiffer.currentList.size

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        if (holder !is ViewHolderBinder<*>) return
        (holder as ViewHolderBinder<Menu>).bind(asyncDataDiffer.currentList[position])

        holder.itemView.findViewById<ImageView>(R.id.iv_favorite_icon).setOnClickListener {
            Toast.makeText(holder.itemView.context, "Ditambahkan ke keranjang", Toast.LENGTH_SHORT).show()

            val item = asyncDataDiffer.currentList[position]
            listener.onItemAddedToCart(item)
        }
    }

    interface OnItemClickedListener<T> {
        fun onItemSelected(item: T)

        fun onItemAddedToCart(item: T)
    }
}
