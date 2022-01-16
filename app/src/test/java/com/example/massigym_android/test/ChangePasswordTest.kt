package com.example.massigym_android.test

import com.example.massigym_android.test_util.ChangePasswordUtil
import com.google.common.truth.Truth
import org.junit.Test


// test sulla validazione dei campi per la modifica della password
class ChangePasswordTest {

    // metodo che verifica che i campi inseriti sono tutti validi
    @Test
    fun changePasswordSubmitted() {
        val check = ChangePasswordUtil.validateChangePasswordInput(
            "123456",
            "123456"
        )
        Truth.assertThat(check).isTrue()
    }

    // metodo che verifica che la password inserita non è valida
    @Test
    fun changePasswordErrorPasswordInvalid() {
        val check = ChangePasswordUtil.validateChangePasswordInput(
            "1234",
            "1234"
        )
        Truth.assertThat(check).isFalse()
    }

    // metodo che verifica che non è stata inserita nessuna password di conferma
    @Test
    fun changePasswordErrorNoConfermaPassword() {
        val check = ChangePasswordUtil.validateChangePasswordInput(
            "123456",
            ""
        )
        Truth.assertThat(check).isFalse()
    }

    // metodo che verifica che la password inserita è diversa dalla password di conferma
    @Test
    fun changePasswordErrorPasswordConferma() {
        val check = ChangePasswordUtil.validateChangePasswordInput(
            "123456",
            "1234567"
        )
        Truth.assertThat(check).isFalse()
    }

}