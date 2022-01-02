package com.example.massigym_android.ui.auth

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.Toast
import com.example.massigym_android.R
import com.example.massigym_android.databinding.ActivityRegistrationBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import java.util.HashMap

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    private lateinit var usernameInput: TextInputEditText
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        usernameInput = binding.registrationUsername
        emailInput = binding.registrationEmail
        passwordInput = binding.registrationPassword

        auth = FirebaseAuth.getInstance()

        val username = usernameInput.text.toString()
        val email = emailInput.text.toString()
        val password = passwordInput.text.toString()

        //al click sul bottone reigstra
        binding.registrationButton.setOnClickListener {
            try {
                signUp(email, password, username)

            } catch (e: Exception) {
                //nel caso in cui l'utente clicchi sul bottone registra senza inserire uno dei campi richiesti
                if (TextUtils.isEmpty(username) && TextUtils.isEmpty(
                        email) && TextUtils.isEmpty(password)
                ) {
                    usernameInput.error = "Per favore inserisci il tuo nome"
                    emailInput.error = "Per favore inserisci la tua mail"
                    passwordInput.error =
                        "Per favore inserisci una password composta da almeno 6 caratteri o numeri"
                    return@setOnClickListener
                } else if (TextUtils.isEmpty(username)) {
                    usernameInput.error = "Per favore inserisci il tuo nome"
                    return@setOnClickListener
                } else if (TextUtils.isEmpty(email)) {
                    emailInput.error = "Per favore inserisci la tua mail"
                    return@setOnClickListener
                } else if (TextUtils.isEmpty(password)) {
                    passwordInput.error =
                        "Per favore inserisci una password composta da almeno 6 caratteri o numeri"
                    return@setOnClickListener
                }
            }
        }

        binding.goToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }


    private fun signUp(email: String, password: String, username: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    postToFirestore(username)
                } else {
                    task.exception!!.printStackTrace()
                    Toast.makeText(
                        this@RegistrationActivity,
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
        userMap["profileImageUrl"] = ""
        FirebaseFirestore.getInstance()
            .collection("users").document(user!!.email!!).set(userMap)
    }


}