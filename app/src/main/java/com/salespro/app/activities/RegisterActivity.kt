package com.salespro.app.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.AttributeSet
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.salespro.app.R
import com.salespro.app.databinding.ActivityRegisterBinding
import com.salespro.app.model.User
import com.salespro.app.util.Resource
import com.salespro.app.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private val TAG = "RegisterActivity"

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

   /* override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inflate the layout using View Binding
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Apply window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Login button click listener
        binding.apply{
            tvDoHaveAnAccount.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }
        }

        binding.apply{
            btnRegister.setOnClickListener {
                val firstName = edFirstName.text.toString()
                val lastName = edLastName.text.toString()
                val email = edEmail.text.toString()
                val password = edPassword.text.toString()

                //Validate the input fields
                if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this@RegisterActivity, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(this@RegisterActivity, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val user = User(firstName, lastName, email)

                viewModel.createAccountWithEmailAndPassword(user,password)
            }
        }

        // Collect the register state from the ViewModel
        lifecycleScope.launchWhenStarted {
            viewModel.register.collect { it ->
                when (it) {
                    is Resource.Loading -> {
                        binding.btnRegister.startAnimation()
                    }
                    is Resource.Success -> {
                        Log.d(TAG, it.data.toString())
                        Snackbar.make(binding.root, "Account created successfully", Snackbar.LENGTH_SHORT).show()
                        binding.btnRegister.revertAnimation()
                        intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                    }
                    is Resource.Error -> {
                        Log.e(TAG, it.message.toString())
                        Toast.makeText(this@RegisterActivity, it.message, Toast.LENGTH_SHORT).show()
                        binding.btnRegister.revertAnimation()
                    }

                    else -> Unit
                }
            }
        }

    }


}