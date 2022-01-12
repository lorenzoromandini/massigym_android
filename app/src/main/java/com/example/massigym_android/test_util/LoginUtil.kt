package com.example.massigym_android.test_util

import androidx.core.util.PatternsCompat

object LoginUtil {

    fun validateLoginInput(
        email: String,
        password: String,
    ): Boolean {
        if (email.isEmpty()) {
            return false
        }
        if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            return false
        }
        if (password.length < 6) {
            return false
        }

        return true
    }

}