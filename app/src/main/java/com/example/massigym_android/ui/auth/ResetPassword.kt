package com.example.massigym_android.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.massigym_android.R
import com.example.massigym_android.databinding.ActivityResetPasswordBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class ResetPassword : AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.toolbarResetPassword.setNavigationOnClickListener { onBackPressed() }

        binding.resetPasswordButton.setOnClickListener {
            resetPassword()
        }
    }

    private fun resetPassword() {
        val email = binding.resetPasswordEmail.text.toString().trim()
        val emailInput = binding.resetPasswordEmailInput

        if (email.isEmpty()) {
            emailInput.error = getString(R.string.emailRequired)
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.error = getString(R.string.emailInvalid)
            return
        }

        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this,
                    getString(R.string.sendResetPassword),
                    Toast.LENGTH_LONG).show()
                onBackPressed()
            } else {
                Toast.makeText(this, getString(R.string.somethingWentWrong), Toast.LENGTH_LONG).show()
            }
        }
    }

}