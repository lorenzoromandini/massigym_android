package com.example.massigym_android

import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.massigym_android.ui.auth.LoginActivity
import com.example.massigym_android.ui.auth.RegistrationActivity
import com.example.massigym_android.ui.common.BottomNavBar
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class ActivityTestUI {

    @Test
    fun checkMainActivityVisibility() {
        val activityScenario = launch(MainActivity::class.java)

        onView(withId(R.id.layout_mainActivity)).check(matches(isDisplayed()))
    }

    @Test
    fun checkLoginActivityVisibility() {
        val activityScenario = launch(LoginActivity::class.java)

        onView(withId(R.id.layout_login)).check(matches(isDisplayed()))
    }

    @Test
    fun checkLoginActivityForgotPassword() {
        val activityScenario = launch(LoginActivity::class.java)

        onView(withId(R.id.goToResetPassword)).check(matches(withText(R.string.forgotPassword)))

        onView(withId(R.id.loginButton)).perform(click())

        onView(withId(R.id.goToResetPassword)).perform(click())
    }

    @Test
    fun checkRegistrationActivityVisibility() {
        val activityScenario = launch(RegistrationActivity::class.java)

        onView(withId(R.id.layout_registration)).check(matches(isDisplayed()))
    }

    @Test
    fun checkBottomNavBarVisibility() {
        val activityScenario = launch(BottomNavBar::class.java)

        onView(withId(R.id.layout_bottom_navigation)).check(matches(isDisplayed()))
    }

}