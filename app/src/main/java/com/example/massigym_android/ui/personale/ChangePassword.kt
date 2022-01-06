package com.example.massigym_android.ui.personale

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.massigym_android.databinding.ActivityChangePasswordBinding
import com.example.massigym_android.ui.auth.LoginActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class ChangePassword : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding

    private lateinit var passwordInput: TextInputEditText
    private lateinit var confermaPasswordInput: TextInputEditText

    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.toolbarChangePassword.setNavigationOnClickListener { onBackPressed() }

        passwordInput = binding.changeNewPassword
        confermaPasswordInput = binding.changeConfirmPassword

        user = FirebaseAuth.getInstance().currentUser!!

        binding.changePasswordButton.setOnClickListener {
            try {
                val password = passwordInput.text.toString().trim()

                changePassword(password)

            } catch (e: Exception) {
                if (TextUtils.isEmpty(passwordInput.text.toString().trim()) ||
                    TextUtils.isEmpty(confermaPasswordInput.text.toString().trim())
                ) {
                    if (TextUtils.isEmpty(passwordInput.text.toString().trim())) {
                        passwordInput.error = "Password richiesta"
                    }
                    if (TextUtils.isEmpty(confermaPasswordInput.text.toString().trim())) {
                        confermaPasswordInput.error = "Conferma Password richiesta"
                    }
                    return@setOnClickListener
                } else if (passwordInput.text.toString()
                        .trim() != confermaPasswordInput.text.toString().trim()
                ) {
                    confermaPasswordInput.error =
                        "Password e Conferma Password non coincidono"
                    return@setOnClickListener
                }
            }
        }

    }

    private fun changePassword(password: String) {
        user.updatePassword(password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Modifica Password effettuata",
                        Toast.LENGTH_SHORT
                    ).show()
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                } else {
                    task.exception!!.printStackTrace()
                    Toast.makeText(
                        this,
                        "La Password non può essere modificata",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}