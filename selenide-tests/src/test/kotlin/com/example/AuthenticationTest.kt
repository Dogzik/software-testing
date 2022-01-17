package com.example

import com.codeborne.selenide.Condition.*
import com.codeborne.selenide.Selenide.*
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import io.qameta.allure.Story
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@Epic("Selenide tests")
@Feature("Authentication tests")
class AuthenticationTest : BaseTest() {
    @BeforeEach
    private fun openPage() {
        open("/")
    }

    private fun checkAlert(expectedMessage: String) {
        val alert = switchTo().alert()
        assertEquals(expectedMessage, alert.text)
        alert.accept()
    }

    @Test
    @Story("Sign in")
    @DisplayName("Unknown user login")
    fun unknownUserLogin() {
        signIn(randomString(10), randomString(10))
        checkAlert("Incorrect credentials")
    }

    @Test
    @Story("Sign up")
    @DisplayName("New user could register")
    fun registerUnknownUser() {
        val loginPage = element(".LoginPage")
        signUp(randomString(10), randomString(10))
        loginPage.shouldHave(disappear)
    }

    @Test
    @Story("Sign in")
    @DisplayName("Registered user can login")
    fun newUserLogin() {
        val login = randomString(10)
        val password = randomString(10)
        signUp(login, password)
        refresh()
        val loginPage = element(".LoginPage")
        signIn(login, password)
        loginPage.shouldHave(disappear)
    }

    @Test
    @Story("Sign in")
    @DisplayName("Registered user can't use wrong password")
    fun wrongPasswordLogin() {
        val login = randomString(10)
        val password = randomString(10)
        signUp(login, password)
        refresh()
        signIn(login, password + "_suffix")
        checkAlert("Incorrect credentials")
    }

    @Test
    @Story("Sign up")
    @DisplayName("User can't register twice")
    fun userRegistersTwice() {
        val login = randomString(10)
        val password = randomString(10)
        signUp(login, password)
        refresh()
        signUp(login, password + "_suffix")
        checkAlert("User $login already exists")
    }
}
