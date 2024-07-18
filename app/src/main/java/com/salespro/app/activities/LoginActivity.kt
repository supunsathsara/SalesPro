package com.salespro.app.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.salespro.app.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_main_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val register = findViewById<TextView>(R.id.tv_dont_have_an_account)
        register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish();
        }

        //! CODE FOR LOGIN BTN LISTENER
        val emailEditText: EditText = findViewById(R.id.ed_email_login)
        val passwordEditText: EditText = findViewById(R.id.ed_password_login)
        val loginButton: CircularProgressButton = findViewById(R.id.btn_login_fragment)

        // Set an OnClickListener on the login button
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Show the email and password as a toast message
            Toast.makeText(this, "Email: $email\nPassword: $password", Toast.LENGTH_LONG).show()
        }
    }


}

