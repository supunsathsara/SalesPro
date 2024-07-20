package com.salespro.app.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.salespro.app.R
import com.salespro.app.databinding.ActivityHomeBinding
import com.salespro.app.util.Resource
import com.salespro.app.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    val viewModel by viewModels<CartViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navController = findNavController(R.id.host_fragment)
        binding.bottomNavigation.setupWithNavController(navController)

        lifecycleScope.launchWhenStarted {
            viewModel.cartProducts.collectLatest{
                when(it){
                    is Resource.Success -> {
                        val count = it.data?.size ?:0
                        if(count != 0){
                            binding.bottomNavigation.getOrCreateBadge(R.id.cartFragment).apply {
                                backgroundColor = resources.getColor(R.color.g_dark_blue)
                                number = count
                            }
                        }
                    }
                    else -> Unit
                }

                }                }
            }


    }
