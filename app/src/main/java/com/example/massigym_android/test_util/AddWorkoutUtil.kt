package com.example.massigym_android.test_util


// object utilizzato per effettuare i test sulla validazione dei campi per l'inserimento di un nuovo workout
object AddWorkoutUtil {

    // funzione che controlla i campi di inserimento
    fun validateAddWorkoutInput(
        nome: String,
        categoria: String,
        descrizione: String,
        durata: String,
        imageUrl: String,
        videoUrl: String,
    ): Boolean {
        if (nome.length < 2) {
            return false
        }
        if (descrizione.length < 10) {
            return false
        }
        if (categoria.isEmpty()) {
            return false
        }
        if (durata.isEmpty()) {
            return false
        }

        return true
    }
}