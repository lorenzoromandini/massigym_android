package com.example.massigym_android.test_util

import androidx.core.util.PatternsCompat


// object utilizzato per effettuare i test sulla validazione dei campi per il login dell'utente
object LoginUtil {

    // funzione che controlla i campi di inserimento
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