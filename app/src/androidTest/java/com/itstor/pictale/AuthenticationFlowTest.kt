package com.itstor.pictale

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.itstor.pictale.data.remote.retrofit.ApiConfig
import com.itstor.pictale.data.remote.retrofit.ApiService
import com.itstor.pictale.di.ApiModule
import com.itstor.pictale.ui.views.auth.AuthActivity
import com.itstor.pictale.ui.views.home.MainActivity
import com.itstor.pictale.utils.EspressoIdlingResource
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@MediumTest
@UninstallModules(ApiModule::class)
@HiltAndroidTest
class LoginLogoutTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val activity = ActivityScenarioRule(AuthActivity::class.java)

    private val mockWebServer = MockWebServer()

    @BindValue
    @JvmField
    val apiService: ApiService = ApiConfig.getApiService("http://localhost:8080/")

    @Before
    fun setUp() {
        mockWebServer.start(8080)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        Intents.init()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        Intents.release()
    }

    @Test
    fun testLoginWithValidData() {
        onView(ViewMatchers.withId(R.id.ed_login_email))
            .perform(ViewActions.typeText("example@gmail.com"))
        onView(ViewMatchers.withId(R.id.ed_login_password))
            .perform(ViewActions.typeText("Password123"))

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("login_success_response.json"))

        mockWebServer.enqueue(mockResponse)

        onView(ViewMatchers.withId(R.id.btn_login))
            .perform(ViewActions.click())

        intended(hasComponent(MainActivity::class.java.name))
    }

    @Test
    fun testLoginWithInvalidData() {
        onView(ViewMatchers.withId(R.id.ed_login_email))
            .perform(ViewActions.typeText("example@gmail.com"))
        onView(ViewMatchers.withId(R.id.ed_login_password))
            .perform(ViewActions.typeText("Password123"))

        val mockResponse = MockResponse()
            .setResponseCode(401)
            .setBody(JsonConverter.readStringFromFile("login_failed_response.json"))

        mockWebServer.enqueue(mockResponse)

        onView(ViewMatchers.withId(R.id.btn_login))
            .perform(ViewActions.click())

        onView(ViewMatchers.withId(R.id.btn_login)).check(matches(isDisplayed()))

    }

    @Test
    fun testLoginLogout() {
        onView(ViewMatchers.withId(R.id.ed_login_email))
            .perform(ViewActions.typeText("example@gmail.com"))
        onView(ViewMatchers.withId(R.id.ed_login_password))
            .perform(ViewActions.typeText("Password123"))

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("login_success_response.json"))

        mockWebServer.enqueue(mockResponse)

        onView(ViewMatchers.withId(R.id.btn_login))
            .perform(ViewActions.click())

        intended(hasComponent(MainActivity::class.java.name))

        onView(ViewMatchers.withId(R.id.action_logout))
            .perform(ViewActions.click())

        intended(hasComponent(AuthActivity::class.java.name))
    }
}