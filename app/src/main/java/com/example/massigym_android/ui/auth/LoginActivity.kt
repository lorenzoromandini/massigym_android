package com.example.massigym_android.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.util.PatternsCompat
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

    fun login(view: View) {

        val email = binding.loginEmail.text.toString().trim()
        val emailInput = binding.emailTextInputLayout
        val password = binding.loginPassword.text.toString().trim()
        val passwordInput = binding.passwordTextInputLayout

        if (email.isEmpty() || !PatternsCompat.EMAIL_ADDRESS.matcher(email)
                .matches() || password.length < 6
        ) {

            if (email.isEmpty()) {
                emailInput.error = getString(R.string.emailRequired)
            }
            if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
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