package com.salespro.app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.salespro.app.R
import com.salespro.app.adapters.CartProductAdapter
import com.salespro.app.adapters.OrdersAdapter
import com.salespro.app.databinding.FragmentOrderDetailsBinding


class OrderDetailsFragment : Fragment() {
    private lateinit var binding: FragmentOrderDetailsBinding
    private val orderAdapter by lazy { OrdersAdapter() }
    private val cartAdapter by lazy { CartProductAdapter(showPlusMinus = false) }

    private val args by navArgs<OrderDetailsFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderDetailsBinding.inflate(inflater, container, false)
        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)?.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOrderDetailsRv()


        cartAdapter.differ.submitList(args.order.products)

        binding.apply {
            tvOrderId.text = args.order.orderId.toString()
            tvTotalPrice.text = "Rs. ${args.order.totalPrice}"

        }

        binding.imageCloseOrder.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    private fun setupOrderDetailsRv() {
        binding.rvProducts.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
        }
    }


}