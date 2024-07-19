package com.salespro.app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.salespro.app.R
import com.salespro.app.databinding.FragmentProductDetailsBinding
import com.salespro.app.model.CartProduct
import com.salespro.app.util.Resource
import com.salespro.app.viewmodel.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {

    private val args by navArgs<ProductDetailsFragmentArgs>()
    private lateinit var binding: FragmentProductDetailsBinding

    private val viewModel by viewModels<DetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product

        binding.imgClose.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnAddToCart.setOnClickListener {
            if (product.qty == 0) {
                Toast.makeText(requireContext(), "Out of stock", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.addUpdateCart(CartProduct(product, 1))
        }

        lifecycleScope.launchWhenStarted {
            viewModel.addToCart.collectLatest {
                when(it){
                    is Resource.Loading -> {
                        binding.btnAddToCart.startAnimation()
                    }
                    is Resource.Success -> {
                        binding.btnAddToCart.revertAnimation()
                        Toast.makeText(requireContext(), "Added to cart", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Error -> {
                        binding.btnAddToCart.revertAnimation()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit

                }
            }
        }

        binding.apply {
            tvProductName.text = product.name
            tvProductDescription.text = product.description
            tvProductPrice.text = "Rs.${product.price}"
            tvProductQty.text = product.qty.toString()
            Glide.with(requireContext()).load(product.imageUrl).into(productImage)
        }
    }

}