package com.example.massigym_android.test_util

import androidx.core.util.PatternsCompat


object RegistrationUtil {

    private val existingUserMail = "test@gmail.com"

    fun validateRegistrationInput(
        username: String,
        email: String,
        password: String,
        confermaPassword: String,
    ): Boolean {
        if (email.isEmpty() || confermaPassword.isEmpty()) {
            return false
        }
        if (username.length < 5) {
            return false
        }
        if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            return false
        }
        if (email in existingUserMail) {
            return false
        }
        if (password.length < 6) {
            return false
        }
        if (confermaPassword != password) {
            return false
        }

        return true
    }

}
