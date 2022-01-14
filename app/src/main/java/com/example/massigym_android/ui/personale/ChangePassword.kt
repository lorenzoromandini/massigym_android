package com.example.massigym_android.ui.personale

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.massigym_android.R
import com.example.massigym_android.databinding.ActivityChangePasswordBinding
import com.example.massigym_android.ui.auth.LoginActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class ChangePassword : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding

    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.toolbarChangePassword.setNavigationOnClickListener { onBackPressed() }

        user = FirebaseAuth.getInstance().currentUser!!

        binding.changePasswordButton.setOnClickListener {
            changePassword()
        }

    }

    private fun changePassword() {

        val password = binding.changeNewPassword.text.toString().trim()
        val passwordInput = binding.changeNewPassword
        val confermaPassword = binding.changeConfirmPassword.text.toString().trim()
        val confermaPasswordInput = binding.changeConfirmPassword

        if (password.length < 6 || confermaPassword.isEmpty() || password != confermaPassword
        ) {
            if (password.length < 6) {
                passwordInput.error = getString(R.string.passwordInvalid)
            }
            if (confermaPassword.isEmpty()) {
                confermaPasswordInput.error = getString(R.string.passwordConfirmRequired)
            }
            if (password != confermaPassword) {
                confermaPasswordInput.error = getString(R.string.passwordNotEquals)
            }
            return
        }

        user.updatePassword(password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        getString(R.string.passwordChangedSuccessfully),
                        Toast.LENGTH_LONG
                    ).show()
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                } else {
                    task.exception!!.printStackTrace()
                    Toast.makeText(
                        this,
                        getString(R.string.somethingWentWrong),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}