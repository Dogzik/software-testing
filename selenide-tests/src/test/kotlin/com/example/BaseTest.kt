package com.example

import com.codeborne.selenide.CollectionCondition
import com.codeborne.selenide.Condition
import com.codeborne.selenide.Selectors.*
import com.codeborne.selenide.Selenide

open class BaseTest {
    private fun fillCredentials(login: String, password: String) {
        val inputs = Selenide.elements(".LoginPage__input input")
        inputs.shouldHave(CollectionCondition.size(2))
        val loginInput = inputs[0]
        val passwordInput = inputs[1]
        loginInput.shouldHave(Condition.attribute("placeholder", "login"))
        passwordInput.shouldHave(Condition.attribute("placeholder", "password"))
        loginInput.value = login
        passwordInput.value = password
        loginInput.shouldHave(Condition.value(login))
        passwordInput.shouldHave(Condition.value(password))
    }

    private fun submitCredentials(type: String, login: String, password: String) {
        fillCredentials(login, password)
        val signButton = Selenide.element(byText(type))
        signButton.click()
    }

    protected fun signIn(login: String, password: String) {
        submitCredentials("Sign in", login, password)
    }

    protected fun signUp(login: String, password: String) {
        submitCredentials("Sign up", login, password)
    }
}
