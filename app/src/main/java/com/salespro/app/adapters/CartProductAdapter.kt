package com.salespro.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.salespro.app.databinding.CartItemBinding
import com.salespro.app.databinding.ProductItemBinding
import com.salespro.app.model.CartProduct


class CartProductAdapter (private val showPlusMinus: Boolean = true):
    RecyclerView.Adapter<CartProductAdapter.CartProductsViewHolder>() {
    var onItemClick: ((CartProduct) -> Unit)? = null

    inner class CartProductsViewHolder(val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root){
            fun bind(cartProduct: CartProduct){
                binding.apply {
                    Glide.with(itemView).load(cartProduct.product.imageUrl).into(imgCartProduct)
                    tvCartProductName.text = cartProduct.product.name
                    tvQuantity.text = cartProduct.quantity.toString()
                    tvProductCartPrice.text = "LKR ${cartProduct.product.price}"

                    if (showPlusMinus) {
                        imgPlus.visibility = View.VISIBLE
                        imgMinus.visibility = View.VISIBLE
                    } else {
                        imgPlus.visibility = View.INVISIBLE
                        imgMinus.visibility = View.INVISIBLE
                    }
                }
            }
        }

    private val diffCallback = object : DiffUtil.ItemCallback<CartProduct>() {
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product.id == newItem.product.id

        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartProductsViewHolder {
        return CartProductsViewHolder(
            CartItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onProductClick: ((CartProduct) -> Unit)? = null
    var onPlusClick: ((CartProduct) -> Unit)? = null
    var onMinusClick: ((CartProduct) -> Unit)? = null



    override fun onBindViewHolder(holder: CartProductsViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener {
            // onItemClick?.invoke(differ.currentList[position])
            onProductClick?.invoke(product)

        }

        holder.binding.imgPlus.setOnClickListener {
            onPlusClick?.invoke(product)

        }

        holder.binding.imgMinus.setOnClickListener {
            onMinusClick?.invoke(product)

        }

        //function to hide the plus and minus buttons from outside
        fun hidePlusMinus(){
            holder.binding.imgPlus.visibility = View.GONE
            holder.binding.imgMinus.visibility = View.GONE
        }


    }

    var hidePlusMinus: (() -> Unit)? = null


}