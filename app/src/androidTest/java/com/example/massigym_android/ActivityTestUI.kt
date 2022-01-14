package com.example.massigym_android

import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.massigym_android.ui.auth.LoginActivity
import com.example.massigym_android.ui.auth.RegistrationActivity
import com.example.massigym_android.ui.common.BottomNavBar
import com.example.massigym_android.ui.personale.ChangePassword
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class ActivityTestUI {

    @Test
    fun checkMainActivityVisibility() {
        launch(MainActivity::class.java)

        onView(withId(R.id.layout_mainActivity)).check(matches(isDisplayed()))
    }

    @Test
    fun checkLoginAndResetActivity() {
        launch(LoginActivity::class.java)

        onView(withId(R.id.layout_login)).check(matches(isDisplayed()))

        onView(withId(R.id.goToResetPassword)).check(matches(withText(R.string.forgotPassword)))

        onView(withId(R.id.loginButton)).perform(click())

        onView(withId(R.id.goToResetPassword)).perform(click())

        onView(withId(R.id.layout_reset)).check(matches(isDisplayed()))

        Espresso.pressBack()

        onView(withId(R.id.layout_login)).check(matches(isDisplayed()))
    }

    @Test
    fun checkRegistrationActivity() {
        launch(RegistrationActivity::class.java)

        onView(withId(R.id.layout_registration)).check(matches(isDisplayed()))

        onView(withId(R.id.registrationButton)).check(matches(withText("Registrati")))

        onView(withId(R.id.registrationConfirmPassword)).check(matches(withHint("Conferma Password")))

    }

    @Test
    fun checkBottomNavBarVisibility() {
        launch(BottomNavBar::class.java)

        onView(withId(R.id.layout_bottom_navigation)).check(matches(isDisplayed()))
    }

    @Test
    fun checkChangePassword() {
        launch(ChangePassword::class.java)

        onView(withId(R.id.layout_changePassword)).check(matches(isDisplayed()))

        onView(withId(R.id.changePasswordButton)).check(matches(withText("Modifica Password")))
    }

}