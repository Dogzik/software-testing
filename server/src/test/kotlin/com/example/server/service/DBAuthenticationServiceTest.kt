package com.example.server.service

import com.example.server.model.Credentials
import com.example.server.repo.AuthenticationRepo
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import io.qameta.allure.Story
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@Epic("Service tests")
@Feature("Authentication tests")
class DBAuthenticationServiceTest {
    private val mockAuthRepo = mockk<AuthenticationRepo>()
    private val authService = DBAuthenticationService(mockAuthRepo)

    @BeforeEach
    fun clearRepo() {
        clearMocks(mockAuthRepo)
    }

    @Test
    @Story("Sign in")
    @DisplayName("Signing in with correct and incorrect credentials")
    fun testLogin() = runBlocking {
        val wrongCredentials = Credentials("false", "false")
        val correctCredentials = Credentials("true", "true")
        coEvery { mockAuthRepo.login(wrongCredentials) } returns false
        coEvery { mockAuthRepo.login(correctCredentials) } returns true
        assertEquals(false, authService.login(wrongCredentials))
        assertEquals(true, authService.login(correctCredentials))
        coVerify(exactly = 1) {
            mockAuthRepo.login(wrongCredentials)
            mockAuthRepo.login(correctCredentials)
        }
    }

    @Test
    @Story("Sign up")
    @DisplayName("Signing up with same credentials twice")
    fun testRegister() = runBlocking {
        val testCredentials = Credentials("aaa", "bbb")
        coEvery { mockAuthRepo.register(testCredentials) } returns true andThen false
        assertEquals(true, authService.register(testCredentials))
        assertEquals(false, authService.register(testCredentials))
        coVerify(exactly = 2) {
            mockAuthRepo.register(testCredentials)
        }
    }
}
