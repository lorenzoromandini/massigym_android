package com.example.massigym_android.test

import com.example.massigym_android.test_util.ChangePasswordUtil
import com.google.common.truth.Truth
import org.junit.Test

class ChangePasswordTest {

    @Test
    fun changePasswordSubmitted() {
        val check = ChangePasswordUtil.validateChangePasswordInput(
            "123456",
            "123456"
        )
        Truth.assertThat(check).isTrue()
    }

    @Test
    fun changePasswordErrorPasswordInvalid() {
        val check = ChangePasswordUtil.validateChangePasswordInput(
            "1234",
            "1234"
        )
        Truth.assertThat(check).isFalse()
    }

    @Test
    fun changePasswordErrorNoConfermaPassword() {
        val check = ChangePasswordUtil.validateChangePasswordInput(
            "123456",
            ""
        )
        Truth.assertThat(check).isFalse()
    }

    @Test
    fun changePasswordErrorPasswordConferma() {
        val check = ChangePasswordUtil.validateChangePasswordInput(
            "123456",
            "1234567"
        )
        Truth.assertThat(check).isFalse()
    }

}