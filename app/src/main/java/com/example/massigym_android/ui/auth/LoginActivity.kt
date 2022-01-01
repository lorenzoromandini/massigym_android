package com.example.massigym_android.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.example.massigym_android.BottomNavBar
import com.example.massigym_android.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.loginButton.setOnClickListener {
            login()
        }

        binding.goToRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, BottomNavBar::class.java)
            startActivity(intent)
        }
    }

    private fun login() {

        val email = binding.loginEmail.text.toString().trim()
        val emailLayout = binding.emailTextInputLayout
        val password = binding.loginPassword.text.toString().trim()
        val passwordLayout = binding.passwordTextInputLayout

        if (TextUtils.isEmpty(email)) {
            emailLayout.error = "Email richiesta"
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.error = "Immettere una Email valida"
            return
        }
        if (TextUtils.isEmpty(password)) {
            passwordLayout.error = "Password richiesta"
            return
        }
        if (password.length < 6) {
            passwordLayout.error = "Immettere una Password valida. (Min. 6 caratteri)"
            return
        }

        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(OnCompleteListener<AuthResult> { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, RegistrationActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(baseContext, "Authentication failed", Toast.LENGTH_LONG).show()
                }
            })
    }


}