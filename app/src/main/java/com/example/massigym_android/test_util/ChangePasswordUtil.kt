package com.example.massigym_android.test_util

object ChangePasswordUtil {

    fun validateChangePasswordInput(
        password: String,
        confermaPassword: String,
    ): Boolean {
        if (password.length < 6) {
            return false
        }
        if (confermaPassword.isEmpty()) {
            return false
        }
        if (password != confermaPassword) {
            return false
        }

        return true
    }
}
