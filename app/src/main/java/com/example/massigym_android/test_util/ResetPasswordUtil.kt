package com.example.massigym_android.test_util

import androidx.core.util.PatternsCompat

object ResetPasswordUtil {

    fun validateResetPasswordInput(
        email: String,
    ): Boolean {
        if (email.isEmpty()) {
            return false
        }
        if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            return false
        }

        return true
    }
}