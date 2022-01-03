package com.example.massigym_android.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.massigym_android.ui.common.BottomNavBar
import com.example.massigym_android.databinding.ActivityRegistrationBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.HashMap

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    private lateinit var usernameInput: TextInputEditText
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var confermaPasswordInput: TextInputEditText

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.goToLogin.setOnClickListener {
            val intent = Intent(this@RegistrationActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        usernameInput = binding.registrationUsername
        emailInput = binding.registrationEmail
        passwordInput = binding.registrationPassword
        confermaPasswordInput = binding.registrationConfirmPassword

        auth = FirebaseAuth.getInstance()

        binding.registrationButton.setOnClickListener {
            try {
                val username = usernameInput.text.toString().trim()
                val email = emailInput.text.toString().trim()
                val password = passwordInput.text.toString().trim()

                signUp(email, password, username)

            } catch (e: Exception) {
                //nel caso in cui l'utente clicchi sul bottone registra senza inserire uno dei campi richiesti
                /*
            if (TextUtils.isEmpty(usernameInput.text.toString().trim()) && TextUtils.isEmpty(
                    emailInput.text.toString().trim()) && TextUtils.isEmpty(passwordInput.text.toString().trim())) {
                usernameInput.error = "Per favore inserisci il tuo nome"
                emailInput.error = "Per favore inserisci la tua mail"
                passwordInput.error =
                    "Per favore inserisci una password composta da almeno 6 caratteri o numeri"
                return@setOnClickListener
            } else if (TextUtils.isEmpty(usernameInput.text.toString().trim())) {
                usernameInput.error = "Per favore inserisci il tuo nome"
                return@setOnClickListener
            } else if (TextUtils.isEmpty(emailInput.text.toString().trim())) {
                emailInput.error = "Per favore inserisci la tua mail"
                return@setOnClickListener
            } else if (TextUtils.isEmpty(passwordInput.text.toString().trim())) {
                passwordInput.error =
                    "Per favore inserisci una password composta da almeno 6 caratteri o numeri"
                return@setOnClickListener
            }
            */
                if (TextUtils.isEmpty(usernameInput.text.toString().trim()) || TextUtils.isEmpty(
                        emailInput.text.toString()
                            .trim()) || TextUtils.isEmpty(passwordInput.text.toString()
                        .trim()) || TextUtils.isEmpty(confermaPasswordInput.text.toString().trim())
                ) {
                    if (TextUtils.isEmpty(usernameInput.text.toString().trim())) {
                        usernameInput.error = "Per favore inserisci il tuo nome"
                    }
                    if (TextUtils.isEmpty(emailInput.text.toString().trim())) {
                        emailInput.error = "Per favore inserisci la tua mail"
                    }
                    if (TextUtils.isEmpty(passwordInput.text.toString().trim())) {
                        passwordInput.error =
                            "Per favore inserisci una password composta da almeno 6 caratteri o numeri"
                    }
                    if (TextUtils.isEmpty(confermaPasswordInput.text.toString().trim())) {
                        confermaPasswordInput.error =
                            "Per favore inserisci la conferma password"
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


    private fun signUp(email: String, password: String, username: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    postToFirestore(username)
                    val intent = Intent(this, BottomNavBar::class.java)
                    startActivity(intent)
                    finish()
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
        userMap["imageUrl"] = ""
        FirebaseFirestore.getInstance()
            .collection("users").document(user!!.email!!).set(userMap)
    }


}