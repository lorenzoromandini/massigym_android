package com.example.massigym_android.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.example.massigym_android.R
import com.example.massigym_android.databinding.ActivityLoginBinding
import com.example.massigym_android.ui.HomeFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    lateinit var email: TextInputEditText
    lateinit var emailLayout: TextInputLayout
    lateinit var password: TextInputEditText
    lateinit var passwordLayout: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        email = binding.loginEmail
        emailLayout = binding.emailTextInputLayout
        password = binding.loginPassword
        passwordLayout = binding.passwordTextInputLayout

        binding.loginButton.setOnClickListener {
            login()
        }

        binding.goToRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login() {
        if(TextUtils.isEmpty(email.text.toString())) {
            emailLayout.error = "Email richiesta"
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email.toString().trim()).matches()) {
            emailLayout.error = "Immettere una Email valida"
        }
            if (TextUtils.isEmpty(password.text.toString())) {
                passwordLayout.error = "Password richiesta"
        }

        if(password.text.toString().trim().length < 6) {
            passwordLayout.error = "Immettere una Password valida. (Min. 6 caratteri)"
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email.toString().trim(), password.text.toString().trim())
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful) {
                    val intent = Intent(this, HomeFragment::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(baseContext, "Authentication failed", Toast.LENGTH_LONG).show()
                }
            }
    }




}