package com.example.server.service

import com.example.server.model.Credentials
import com.example.server.repo.AuthenticationRepo
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DBAuthenticationServiceTest {
    private val mockAuthRepo = mockk<AuthenticationRepo>()
    private val authService = DBAuthenticationService(mockAuthRepo)

    @BeforeEach
    fun clearRepo() {
        clearMocks(mockAuthRepo)
    }

    @Test
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
