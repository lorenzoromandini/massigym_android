package com.example.massigym_android.test

import com.example.massigym_android.test_util.RegistrationUtil
import org.junit.Test
import com.google.common.truth.Truth.assertThat

class RegistrationTest {

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

    @Test
    fun registrationErrorUsernameExisting() {
        val check = RegistrationUtil.validateRegistrationInput(
            "test",
            "mariorossi@gmail.com",
            "123456",
            "123456"
        )
        assertThat(check).isFalse()
    }

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

    @Test
    fun registrationErrorNoConfermaPassword() {
        val check = RegistrationUtil.validateRegistrationInput(
            "",
            "mariorossi@gmail.com",
            "123456",
            ""
        )
        assertThat(check).isFalse()
    }

    @Test
    fun registrationErrorPasswordConferma() {
        val check = RegistrationUtil.validateRegistrationInput(
            "",
            "mariorossi@gmail.com",
            "123456",
            "1234567"
        )
        assertThat(check).isFalse()
    }

}