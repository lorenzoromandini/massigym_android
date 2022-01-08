package com.example.massigym_android.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.massigym_android.ui.common.BottomNavBar
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
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.goToResetPassword.setOnClickListener {
            val intent = Intent(this, ResetPassword::class.java)
            startActivity(intent)
        }
    }

    private fun login() {

        val email = binding.loginEmail.text.toString().trim()
        val emailInput = binding.emailTextInputLayout
        val password = binding.loginPassword.text.toString().trim()
        val passwordInput = binding.passwordTextInputLayout

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email)
                .matches() || password.isEmpty() || password.length < 6
        ) {

            if (email.isEmpty()) {
                emailInput.error = "Email richiesta"
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.error = "Inserisci un formato email valido"
            }
            if (password.isEmpty()) {
                passwordInput.error = "Password richiesta"
            }
            if (password.length < 6) {
                passwordInput.error = "Immettere una Password valida. (Min. 6 caratteri)"
            }
            return
        }

        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(OnCompleteListener<AuthResult> { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, BottomNavBar::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Login effettuato", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                } else {
                    Toast.makeText(baseContext, "Authentication failed", Toast.LENGTH_LONG).show()
                }
            })
    }


}