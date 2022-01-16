package com.example.server.controller

import com.example.server.configuration.MockkServiceTestConfiguration
import com.example.server.configuration.SpringMockkServiceTestConfiguration
import com.example.server.model.Credentials
import com.example.server.service.AuthenticationService
import io.mockk.coEvery
import io.mockk.coVerify
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import io.qameta.allure.Story
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.post

@WebMvcTest(AuthenticationController::class)
@Import(MockkServiceTestConfiguration::class, SpringMockkServiceTestConfiguration::class)
@Epic("Controller tests")
@Feature("Authentication tests")
class AuthenticationControllerTest {
    @Autowired
    private lateinit var authService: AuthenticationService

    @Autowired
    private lateinit var mockMvc: MockMvc

    private fun sendAuthRequest(url: String, credentials: Credentials) =
        mockMvc.post(url) {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = Json.encodeToString(credentials)
        }.asyncDispatch()

    private fun sendLoginRequest(credentials: Credentials): ResultActionsDsl = sendAuthRequest("/login", credentials)

    private fun sendRegisterRequest(credentials: Credentials): ResultActionsDsl =
        sendAuthRequest("/register", credentials)

    @Test
    @Story("Sign in")
    @DisplayName("Signing in with correct and incorrect credentials")
    fun testLogin() {
        val wrongCredentials = Credentials("false", "false")
        val correctCredentials = Credentials("true", "true")
        coEvery { authService.login(wrongCredentials) } returns false
        coEvery { authService.login(correctCredentials) } returns true
        sendLoginRequest(wrongCredentials).andExpect {
            status { isForbidden() }
            content { string("Incorrect credentials") }
        }
        sendLoginRequest(correctCredentials).andExpect {
            status { isOk() }
            content { string("Success") }
        }
        coVerify(exactly = 1) {
            authService.login(wrongCredentials)
            authService.login(correctCredentials)
        }
    }

    @Test
    @Story("Sign up")
    @DisplayName("Signing up with same credentials twice")
    fun testRegister() {
        val credentials = Credentials("aaa", "bbb")
        coEvery { authService.register(credentials) } returns true andThen false
        sendRegisterRequest(credentials).andExpect {
            status { isOk() }
            content { string("Success") }
        }
        sendRegisterRequest(credentials).andExpect {
            status { isConflict() }
            content { string("User ${credentials.login} already exists") }
        }
        coVerify(exactly = 2) {
            authService.register(credentials)
        }
    }
}
