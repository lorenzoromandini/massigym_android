package com.example.massigym_android.test

import com.example.massigym_android.test_util.LoginUtil
import com.google.common.truth.Truth
import org.junit.Test

class LoginTest {

    @Test
    fun loginSubmitted() {
        val check = LoginUtil.validateLoginInput(
            "mariorossi@gmail.com",
            "123456"
        )
        Truth.assertThat(check).isTrue()
    }

    @Test
    fun loginErrorNoEmail() {
        val check = LoginUtil.validateLoginInput(
            "",
            "123456"
        )
        Truth.assertThat(check).isFalse()
    }

    @Test
    fun loginErrorEmailInvalid() {
        val check = LoginUtil.validateLoginInput(
            "mariorossiagmail.com",
            "123456",
        )
        Truth.assertThat(check).isFalse()
    }

    @Test
    fun loginErrorPasswordInvalid() {
        val check = LoginUtil.validateLoginInput(
            "mariorossi@gmail.com",
            "0"
        )
        Truth.assertThat(check).isFalse()
    }
}