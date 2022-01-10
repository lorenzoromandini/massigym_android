package com.example.massigym_android

import android.util.Patterns
import com.google.firebase.auth.ktx.userProfileChangeRequest

object RegistrationUtil {

    private val existingUser = "test@gmail.com"

    fun validateRegistrationInput(username: String, email: String, password: String, confermaPassword: String): Boolean {
        if(username.isEmpty() || email.isEmpty() || password.isEmpty() || confermaPassword.isEmpty()) {
            return false
        }
        if(username.length < 5) {
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false
        }
        if(email in existingUser) {
            return false
        }
        if(password.length < 6) {
            return false
        }
        if(confermaPassword != password) {
            return false
        }

        return true
    }
}