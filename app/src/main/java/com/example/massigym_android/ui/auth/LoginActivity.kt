package com.example.massigym_android.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.massigym_android.R
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
                .matches() || password.length < 6
        ) {

            if (email.isEmpty()) {
                emailInput.error = getString(R.string.emailRequired)
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.error = getString(R.string.emailInvalid)
            }
            if (password.length < 6) {
                passwordInput.error = getString(R.string.passwordInvalid)
            }
            return
        }

        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(OnCompleteListener<AuthResult> { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, BottomNavBar::class.java)
                    startActivity(intent)
                    Toast.makeText(this, getString(R.string.loginDone), Toast.LENGTH_SHORT)
                        .show()
                    finish()
                } else {
                    Toast.makeText(baseContext, getString(R.string.somethingWentWrong), Toast.LENGTH_LONG).show()
                }
            })
    }


}