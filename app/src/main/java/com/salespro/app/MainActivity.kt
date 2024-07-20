package com.salespro.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.salespro.app.activities.HomeActivity
import com.salespro.app.activities.LoginActivity
import com.salespro.app.viewmodel.LaunchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<LaunchViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // You can set an initial layout here if needed
        // setContentView(R.layout.activity_main)

        // Observe the navigate StateFlow
        lifecycleScope.launch {
            viewModel.navigate.collectLatest { navigation ->
                when (navigation) {
                    LaunchViewModel.NAVIGATE_TO_LOGIN -> {
                        val intent = Intent(this@MainActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish() // To prevent the user from returning to this activity
                    }
                    LaunchViewModel.NAVIGATE_TO_HOME -> {
                        val intent = Intent(this@MainActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish() // To prevent the user from returning to this activity
                    }
                }
            }
        }
    }
}