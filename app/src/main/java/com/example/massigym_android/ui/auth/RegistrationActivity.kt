package com.example.massigym_android.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.util.PatternsCompat
import com.example.massigym_android.R
import com.example.massigym_android.databinding.ActivityRegistrationBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.SignInMethodQueryResult
import com.google.firebase.firestore.FirebaseFirestore
import java.util.HashMap

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.goToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.registrationButton.setOnClickListener {
            signUp()
        }

    }

    private fun signUp() {

        val username = binding.registrationUsername.text.toString().trim()
        val usernameInput = binding.registrationUsername
        val email = binding.registrationEmail.text.toString().trim()
        val emailInput = binding.registrationEmail
        val password = binding.registrationPassword.text.toString().trim()
        val passwordInput = binding.registrationPassword
        val confermaPassword = binding.registrationConfirmPassword.text.toString().trim()
        val confermaPasswordInput = binding.registrationConfirmPassword

        if (username.length < 5 && email.isEmpty() || !PatternsCompat.EMAIL_ADDRESS.matcher(
                email)
                .matches() || password.length < 6 || confermaPassword.isEmpty() || password != confermaPassword
        ) {
            if (username.length < 5) {
                usernameInput.error = getString(R.string.usernameInvalid)
            }
            if (email.isEmpty()) {
                emailInput.error = getString(R.string.emailRequired)
            }
            if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.error = getString(R.string.emailInvalid)
            }
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

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    postToFirestore(username)
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, getString(R.string.registrationSuccess), Toast.LENGTH_LONG)
                        .show()
                    finish()
                } else {
                    task.exception!!.printStackTrace()
                    Toast.makeText(
                        this,
                        "${getString(R.string.somethingWentWrong)}. È possibile che un utente si sia già registrato con questa Email",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }


    private fun postToFirestore(username: String) {
        val user = FirebaseAuth.getInstance().currentUser
        val userMap: MutableMap<String, Any> = HashMap()
        userMap["username"] = username
        userMap["imageUrl"] = ""
        FirebaseFirestore.getInstance()
            .collection("users").document(user!!.email!!).set(userMap)
    }

}