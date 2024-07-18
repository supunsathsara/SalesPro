package com.salespro.app.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.google.android.material.snackbar.Snackbar
import com.salespro.app.R
import com.salespro.app.databinding.ActivityLoginBinding

import com.salespro.app.util.Resource
import com.salespro.app.util.setupBottomSheetDialog
import com.salespro.app.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"

    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inflate the layout using View Binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Apply window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_main_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize the views
        val emailEditText = findViewById<EditText>(R.id.ed_email_login)
        val passwordEditText = findViewById<EditText>(R.id.ed_password_login)
        val loginButton = findViewById<CircularProgressButton>(R.id.btn_login)
        val registerTextView = findViewById<TextView>(R.id.tv_dont_have_an_account)

        binding.apply {
            btnLogin.setOnClickListener {
                val email = edEmailLogin.text.toString()
                val password = edPasswordLogin.text.toString()

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this@LoginActivity, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                viewModel.login(email, password)
            }
        }

        // Set the click listener for the register text view
        registerTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.tvForgotPassword.setOnClickListener {
           setupBottomSheetDialog {  email ->
               viewModel.resetPassword(email)

           }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.resetPassword.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        binding.btnLogin.startAnimation()
                    }
                    is Resource.Success -> {
                        binding.btnLogin.revertAnimation()
                       Snackbar.make(binding.root, "Reset password email sent", Snackbar.LENGTH_SHORT).show()
                    }
                    is Resource.Error -> {
                        binding.btnLogin.revertAnimation()
                        resource.message?.let { Log.e(TAG, it) }
                        Toast.makeText(this@LoginActivity, resource.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        // Observe the login flow
        lifecycleScope.launchWhenStarted {
            viewModel.login.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        binding.btnLogin.startAnimation()
                    }
                    is Resource.Success -> {
                       binding.btnLogin.revertAnimation()

                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    is Resource.Error -> {
                        binding.btnLogin.revertAnimation()
                        resource.message?.let { Log.e(TAG, it) }


                        Toast.makeText(this@LoginActivity, resource.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
    }


}

