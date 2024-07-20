package com.salespro.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.salespro.app.databinding.OrderItemBinding
import com.salespro.app.databinding.ProductItemBinding
import com.salespro.app.model.Order
import com.salespro.app.model.Product

class OrdersAdapter() :
    RecyclerView.Adapter<OrdersAdapter.OrderAdapterViewHolder>() {
    var onItemClick: ((Product) -> Unit)? = null

    inner class OrderAdapterViewHolder(val binding: OrderItemBinding) :
        RecyclerView.ViewHolder(binding.root){
            fun bind(order: Order){
                binding.apply {

                    tvOrderId.text = order.orderId.toString()
                    tvOrderDate.text = order.orderDate


                }
            }
        }

    private val diffCallback = object : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.products == newItem.products

        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderAdapterViewHolder {
        return OrderAdapterViewHolder(
            OrderItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onClick: ((Order) -> Unit)? = null



    override fun onBindViewHolder(holder: OrderAdapterViewHolder, position: Int) {
        val order = differ.currentList[position]
        holder.bind(order)

        holder.itemView.setOnClickListener {
            // onItemClick?.invoke(differ.currentList[position])
            onClick?.invoke(order)
        }

        }


    }

