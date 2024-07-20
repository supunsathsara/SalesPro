package com.salespro.app.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.salespro.app.R
import com.salespro.app.adapters.CartProductAdapter
import com.salespro.app.databinding.FragmentCartBinding
import com.salespro.app.firebaseDatabase.FirebaseCommon
import com.salespro.app.util.Resource
import com.salespro.app.util.VerticalItemDecoration
import com.salespro.app.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private val cartAdapter by lazy { CartProductAdapter() }
    private val viewModel by activityViewModels<CartViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCartRv()

        lifecycleScope.launchWhenStarted {
            viewModel.productPrice.collectLatest { price ->
                price?.let {
                    binding.tvTotalprice.text = "Rs.$price"

                }
            }
        }

        cartAdapter.onProductClick = {
            val b = Bundle().apply { putParcelable("product", it.product) }
            findNavController().navigate(R.id.action_cartFragment_to_productDetailsFragment, b)
        }

        cartAdapter.onPlusClick = {
            viewModel.changeQuantity(it, FirebaseCommon.QuantityChanging.INCREASE)
        }

        cartAdapter.onMinusClick = {
            viewModel.changeQuantity(it, FirebaseCommon.QuantityChanging.DECREASE)
        }

        binding.imgCloseCart.setOnClickListener {
            findNavController().popBackStack()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.deleteDialog.collectLatest {
                var alertDialog = AlertDialog.Builder(requireContext()).apply {
                    setTitle("Delete Product")
                    setMessage("Are you sure you want to delete this product?")
                    setPositiveButton("Yes") { dialog, _ ->
                        viewModel.deleteProduct(it)
                        dialog.dismiss()
                    }
                    setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                }
                alertDialog.create()
                alertDialog.show()
            }
        }



        lifecycleScope.launchWhenStarted {
            viewModel.cartProducts.collectLatest {
                when(it){
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        if(it.data!!.isEmpty()){
                            showEmpty()
                        }else {
                            hideEmpty()
                            cartAdapter.differ.submitList(it.data)
                        }
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }

        }
    }

    private fun hideEmpty() {
        binding.apply {
            tvEmptyCart.visibility = View.GONE
           imgEmptyBoxTexture.visibility = View.GONE
            imgEmptyBox.visibility = View.GONE

            rvCart.visibility = View.VISIBLE
            tvTotalprice.visibility = View.VISIBLE
            tvTotaltxt.visibility = View.VISIBLE
            btnCheckout.visibility = View.VISIBLE
        }
    }

    private fun showEmpty() {
        binding.apply {
            tvEmptyCart.visibility = View.VISIBLE
            imgEmptyBoxTexture.visibility = View.VISIBLE
            imgEmptyBox.visibility = View.VISIBLE

            rvCart.visibility = View.GONE
            tvTotalprice.visibility = View.GONE
            tvTotaltxt.visibility = View.GONE
            btnCheckout.visibility = View.GONE
        }
    }

    private fun setupCartRv(){
        binding.rvCart.apply {
            layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
            adapter = cartAdapter
            addItemDecoration(VerticalItemDecoration())
        }
    }

}