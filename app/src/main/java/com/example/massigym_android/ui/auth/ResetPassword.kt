package com.example.massigym_android.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.massigym_android.databinding.ActivityResetPasswordBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class ResetPassword : AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordBinding

    private lateinit var emailInput: TextInputEditText

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.toolbarResetPassword.setNavigationOnClickListener { onBackPressed() }

        emailInput = binding.resetPasswordEmail

        auth = FirebaseAuth.getInstance()

        binding.resetPasswordButton.setOnClickListener {
            resetPassword()
        }
    }

    private fun resetPassword() {
        val email = emailInput.text.toString().trim()

        if (email.isEmpty()) {
            emailInput.error = "Email richiesta"
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.error = "Inserisci un formato email valido"
            return
        }

        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this,
                    "Controlla la tua mail per resettare la password",
                    Toast.LENGTH_LONG).show()
                onBackPressed()
            } else {
                Toast.makeText(this, "Riprova, qualcosa è andato storto", Toast.LENGTH_LONG).show()
            }
        }
    }

}