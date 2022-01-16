package com.example.massigym_android.test_util


// object utilizzato per effettuare i test sulla validazione dei campi per la modifica della password
object ChangePasswordUtil {

    // funzione che controlla i campi di inserimento
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
