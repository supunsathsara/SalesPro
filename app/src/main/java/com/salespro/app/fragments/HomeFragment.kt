package com.salespro.app.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.salespro.app.R
import com.salespro.app.adapters.ProductsRecyclerAdapter
import com.salespro.app.databinding.FragmentHomeBinding
import com.salespro.app.util.Resource
import com.salespro.app.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val TAG = "HomeFragment"
    private lateinit var binding: FragmentHomeBinding
    private lateinit var productAdapter: ProductsRecyclerAdapter

    private val viewModel by viewModels<HomeViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupProductsRv()

        productAdapter.onClick = {
            Log.d(TAG, "onViewCreated: ${it.name}")
            val b = Bundle().apply { putParcelable("product",it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.products.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressbar2.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        productAdapter.differ.submitList(it.data)
                        binding.progressbar2.visibility = View.GONE
                    }

                    is Resource.Error -> {
                        binding.progressbar2.visibility = View.GONE
                        Log.e(TAG, "onViewCreated: ${it.message}")
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit

                }
            }
        }


    }

    override fun onResume() {
        super.onResume()
        val bottomNavigation =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.visibility = View.VISIBLE
    }

    private fun setupProductsRv() {
        productAdapter = ProductsRecyclerAdapter()
        binding.rvChairs.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = productAdapter
        }
    }
}