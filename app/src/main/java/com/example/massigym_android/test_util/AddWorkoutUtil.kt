package com.example.massigym_android.test_util

object AddWorkoutUtil {

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