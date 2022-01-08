package com.example.massigym_android.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.massigym_android.databinding.ActivityRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
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
        val usernameInput = binding.usernameTextInputLayout
        val email = binding.registrationEmail.text.toString().trim()
        val emailInput = binding.emailTextInputLayout
        val password = binding.registrationPassword.text.toString().trim()
        val passwordInput = binding.passwordTextInputLayout
        val confermaPassword = binding.registrationConfirmPassword.text.toString().trim()
        val confermaPasswordInput = binding.confermaPasswordTextInputLayout

        if (username.isEmpty() || username.length < 5 || email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(
                email)
                .matches() || password.isEmpty() || password.length < 6 || confermaPassword.isEmpty() || password != confermaPassword
        ) {
            if (username.isEmpty()) {
                usernameInput.error = "Username richiesto"
            }
            if (username.length < 5) {
                usernameInput.error = "Immettere uno Username valido. (Min. 5 caratteri)"
            }
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
            if (confermaPassword.isEmpty()) {
                confermaPasswordInput.error = "Conferma Password richiesta"
            }
            if (password != confermaPassword) {
                confermaPasswordInput.error = "Le Password non coincidono"
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
                    Toast.makeText(this, "Registrazione effettuata", Toast.LENGTH_LONG)
                        .show()
                    finish()
                } else {
                    task.exception!!.printStackTrace()
                    Toast.makeText(
                        this,
                        "Registration failed",
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