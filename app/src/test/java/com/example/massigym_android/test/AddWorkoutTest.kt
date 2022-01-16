package com.example.massigym_android.test

import com.example.massigym_android.test_util.AddWorkoutUtil
import com.google.common.truth.Truth
import org.junit.Test


// test sulla validazione dei campi per l'inserimento di un nuovo workout
class AddWorkoutTest {

    val imageUrl =
        "https://www.casadivita.despar.it/app/uploads/2016/12/effetti-negativi-di-allenamento-al-limite.jpg"
    val videoUrl =
        "https://fitterz.it/wp-content/uploads/2021/01/Allenamento-HIIT-per-Dimagrire-con-video.mp4"

    // metodo che verifica che i campi inseriti sono tutti validi
    @Test
    fun addWorkoutSubmitted() {
        val check = AddWorkoutUtil.validateAddWorkoutInput(
            "Corsa",
            "cardio",
            "Correre in strada o su un tapis-roulant a una velocità media di 7 km/h",
            "600",
            imageUrl,
            ""
        )
        Truth.assertThat(check).isTrue()
    }

    // metodo che verifica che il nome inserito non è valido
    @Test
    fun addWorkoutErrorNameInvalid() {
        val check = AddWorkoutUtil.validateAddWorkoutInput(
            "",
            "legs",
            "Piegare le ginocchia ad angolo retto",
            "60",
            "",
            ""
        )
        Truth.assertThat(check).isFalse()
    }

    // metodo che verifica che la descrizione inserita non è valida
    @Test
    fun addWorkoutErrorDescriptionInvalid() {
        val check = AddWorkoutUtil.validateAddWorkoutInput(
            "Flessioni",
            "arms",
            "Braccia",
            "180",
            "",
            videoUrl
        )
        Truth.assertThat(check).isFalse()
    }

    // metodo che verifica che non è stata inserita nessuna categoria
    @Test
    fun addWorkoutErrorNoCategory() {
        val check = AddWorkoutUtil.validateAddWorkoutInput(
            "Plank",
            "",
            " Entrambe le mani devono toccare i fianchi",
            "120",
            imageUrl,
            videoUrl
        )
        Truth.assertThat(check).isFalse()
    }

    // metodo che verifica che non è stata inserita nessuna durata
    @Test
    fun addWorkoutErrorNoDuration() {
        val check = AddWorkoutUtil.validateAddWorkoutInput(
            "addominali",
            "cardio",
            "Fare più addominali possibili nel tempo stabilito",
            "",
            "",
            ""
        )
        Truth.assertThat(check).isFalse()
    }


}