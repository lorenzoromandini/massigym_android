package com.example.massigym_android.test

import com.example.massigym_android.test_util.ResetPasswordUtil
import com.google.common.truth.Truth
import org.junit.Test


// test sulla validazione dei campi per il reset della password
class ResetPasswordTest {

    // metodo che verifica che i campi inseriti sono tutti validi
    @Test
    fun resetPasswordSubmitted() {
        val check = ResetPasswordUtil.validateResetPasswordInput(
            "test@gmail.com",
        )
        Truth.assertThat(check).isTrue()
    }

    // metodo che verifica che non è stata inserita nessuna email
    @Test
    fun resetPasswordErrorNoEmail() {
        val check = ResetPasswordUtil.validateResetPasswordInput(
            "",
        )
        Truth.assertThat(check).isFalse()
    }

    // metodo che verifica che è stata inserita una email non valida
    @Test
    fun resetPasswordErrorEmailInvalid() {
        val check = ResetPasswordUtil.validateResetPasswordInput(
            "test",
        )
        Truth.assertThat(check).isFalse()
    }
}