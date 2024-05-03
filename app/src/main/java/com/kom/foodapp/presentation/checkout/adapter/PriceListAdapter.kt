package com.kom.foodapp.presentation.checkout.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kom.foodapp.data.model.PriceItem
import com.kom.foodapp.databinding.ItemPriceBinding
import com.kom.foodapp.utils.formatToRupiah

class PriceListAdapter(private val itemClick: (PriceItem) -> Unit) :
    RecyclerView.Adapter<PriceListAdapter.PriceItemViewHolder>() {
    private val dataDiffer =
        AsyncListDiffer(
            this,
            object : DiffUtil.ItemCallback<PriceItem>() {
                override fun areItemsTheSame(
                    oldItem: PriceItem,
                    newItem: PriceItem,
                ): Boolean {
                    return oldItem.name == newItem.name
                }

                override fun areContentsTheSame(
                    oldItem: PriceItem,
                    newItem: PriceItem,
                ): Boolean {
                    return oldItem.hashCode() == newItem.hashCode()
                }
            },
        )

    fun submitData(data: List<PriceItem>) {
        dataDiffer.submitList(data)
    }

    class PriceItemViewHolder(
        private val binding: ItemPriceBinding,
        val itemClick: (PriceItem) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PriceItem) {
            with(item) {
                binding.textManuItem.text = item.name
                binding.textTotalPaymentItem.text = item.total.formatToRupiah()
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PriceItemViewHolder {
        val binding = ItemPriceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PriceItemViewHolder(binding, itemClick)
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    override fun onBindViewHolder(
        holder: PriceItemViewHolder,
        position: Int,
    ) {
        holder.bind(dataDiffer.currentList[position])
    }
}
