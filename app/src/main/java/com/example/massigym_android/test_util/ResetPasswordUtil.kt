package com.example.massigym_android.test_util

import androidx.core.util.PatternsCompat


// object utilizzato per effettuare i test sulla validazione dei campi per il reset della password
object ResetPasswordUtil {

    // funzione che controlla i campi di inserimento
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