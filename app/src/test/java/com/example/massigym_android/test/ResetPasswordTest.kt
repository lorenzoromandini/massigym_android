package com.example.massigym_android.test

import com.example.massigym_android.test_util.ResetPasswordUtil
import com.google.common.truth.Truth
import org.junit.Test

class ResetPasswordTest {

    @Test
    fun resetPasswordSubmitted() {
        val check = ResetPasswordUtil.validateResetPasswordInput(
            "test@gmail.com",
        )
        Truth.assertThat(check).isTrue()
    }

    @Test
    fun resetPasswordErrorNoEmail() {
        val check = ResetPasswordUtil.validateResetPasswordInput(
            "",
        )
        Truth.assertThat(check).isFalse()
    }

    @Test
    fun resetPasswordErrorEmailInvalid() {
        val check = ResetPasswordUtil.validateResetPasswordInput(
            "test",
        )
        Truth.assertThat(check).isFalse()
    }
}