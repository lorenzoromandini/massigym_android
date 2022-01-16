package com.example.massigym_android.test

import com.example.massigym_android.test_util.LoginUtil
import com.google.common.truth.Truth
import org.junit.Test


// test sulla validazione dei campi per il login
class LoginTest {

    // metodo che verifica che i campi inseriti sono tutti validi
    @Test
    fun loginSubmitted() {
        val check = LoginUtil.validateLoginInput(
            "mariorossi@gmail.com",
            "123456"
        )
        Truth.assertThat(check).isTrue()
    }

    // metodo che verifica che non è stata inserita nessuna email
    @Test
    fun loginErrorNoEmail() {
        val check = LoginUtil.validateLoginInput(
            "",
            "123456"
        )
        Truth.assertThat(check).isFalse()
    }

    // metodo che verifica che è stata inserita una email non valida
    @Test
    fun loginErrorEmailInvalid() {
        val check = LoginUtil.validateLoginInput(
            "mariorossiagmail.com",
            "123456",
        )
        Truth.assertThat(check).isFalse()
    }

    // metodo che verifica che la password inserita non è valida
    @Test
    fun loginErrorPasswordInvalid() {
        val check = LoginUtil.validateLoginInput(
            "mariorossi@gmail.com",
            "0"
        )
        Truth.assertThat(check).isFalse()
    }
}