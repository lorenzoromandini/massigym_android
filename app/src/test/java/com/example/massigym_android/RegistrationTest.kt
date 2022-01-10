package com.example.massigym_android

import org.junit.Test
import com.google.common.truth.Truth.assertThat

class RegistrationTest {

    @Test
    fun checkRegistration() {

        val check = RegistrationUtil.validateRegistrationInput(
            "UsernameTest",
            "test@gmail.com",
            "test1234",
            "test1234"
        )
        assertThat(check).isFalse()

    }

}