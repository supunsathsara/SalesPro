package com.salespro.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.salespro.app.databinding.ProductItemBinding
import com.salespro.app.model.Product

class ProductsRecyclerAdapter():
    RecyclerView.Adapter<ProductsRecyclerAdapter.BestProductsRecyclerAdapterViewHolder>() {
    var onItemClick: ((Product) -> Unit)? = null

    inner class BestProductsRecyclerAdapterViewHolder(val binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id

        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BestProductsRecyclerAdapterViewHolder {
        return BestProductsRecyclerAdapterViewHolder(
            ProductItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onClick: ((Product) -> Unit)? = null



    override fun onBindViewHolder(holder: BestProductsRecyclerAdapterViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.binding.apply {
            Glide.with(holder.itemView).load(product.imageUrl).into(imgProduct)
            tvName.text = product.name
            tvPrice.text = "LKR ${product.price}"
            tvQty.text = "Qty: ${product.qty}"
        }

        holder.itemView.setOnClickListener {
           // onItemClick?.invoke(differ.currentList[position])
            onClick?.invoke(product)

        }
    }


}

