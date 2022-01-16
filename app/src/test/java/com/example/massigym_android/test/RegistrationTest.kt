package com.example.massigym_android.test

import com.example.massigym_android.test_util.RegistrationUtil
import org.junit.Test
import com.google.common.truth.Truth.assertThat


// test sulla validazione dei campi per a registrazione di un nuovo utente
class RegistrationTest {

    // metodo che verifica che i campi inseriti sono tutti validi
    @Test
    fun registrationSubmitted() {
        val check = RegistrationUtil.validateRegistrationInput(
            "Mario Rossi",
            "mariorossi@gmail.com",
            "123456",
            "123456"
        )
        assertThat(check).isTrue()
    }

    // metodo che verifica che lo username inserito non è valido
    @Test
    fun registrationErrorUsernameInvalid() {
        val check = RegistrationUtil.validateRegistrationInput(
            "",
            "mariorossi@gmail.com",
            "123456",
            "123456"
        )
        assertThat(check).isFalse()
    }

    // metodo che verifica che non è stata inserita nessuna email
    @Test
    fun registrationErrorNoEmail() {
        val check = RegistrationUtil.validateRegistrationInput(
            "mario rossi",
            "",
            "123456",
            "123456"
        )
        assertThat(check).isFalse()
    }

    // metodo che verifica che è stata inserita una email non valida
    @Test
    fun registrationErrorEmailInvalid() {
        val check = RegistrationUtil.validateRegistrationInput(
            "mario",
            "mariorossiagmail.com",
            "123456",
            "123456"
        )
        assertThat(check).isFalse()
    }

    // metodo che verifica che è stata inserita una email già registrata
    @Test
    fun registrationErroEmailExisting() {
        val check = RegistrationUtil.validateRegistrationInput(
            "mariorossi",
            "test@gmail.com",
            "123456",
            "123456"
        )
        assertThat(check).isFalse()
    }

    // metodo che verifica che la password inserita non è valida
    @Test
    fun registrationErrorPasswordInvalid() {
        val check = RegistrationUtil.validateRegistrationInput(
            "mariorossi",
            "mariorossi@gmail.com",
            "1234",
            "1234"
        )
        assertThat(check).isFalse()
    }

    // metodo che verifica che non è stata inserita nessuna password di conferma
    @Test
    fun registrationErrorNoConfermaPassword() {
        val check = RegistrationUtil.validateRegistrationInput(
            "mariorossi",
            "mariorossi@gmail.com",
            "123456",
            ""
        )
        assertThat(check).isFalse()
    }

    // metodo che verifica che la password inserita è diversa dalla password di conferma
    @Test
    fun registrationErrorPasswordConferma() {
        val check = RegistrationUtil.validateRegistrationInput(
            "mariorossi",
            "mariorossi@gmail.com",
            "123456",
            "1234567"
        )
        assertThat(check).isFalse()
    }

}