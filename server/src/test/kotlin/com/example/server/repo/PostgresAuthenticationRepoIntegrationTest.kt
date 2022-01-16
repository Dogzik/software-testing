package com.example.server.repo

import com.example.server.config.DatabaseConfig
import com.example.server.model.Credentials
import com.example.server.utils.getContainerWithConfig
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import io.qameta.allure.Story
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.testcontainers.containers.PostgreSQLContainer

@Epic("Repo tests")
@Feature("Authentication repo integration tests")
class PostgresAuthenticationRepoIntegrationTest {
    private lateinit var config: DatabaseConfig
    private lateinit var container: PostgreSQLContainer<*>
    private lateinit var authRepo: AuthenticationRepo

    @BeforeEach
    fun initContainers() = runBlocking {
        val (newConfig, newContainer) = getContainerWithConfig()
        config = newConfig
        container = newContainer
        authRepo = PostgresAuthenticationRepo(PostgresConnectionProvider(config))
    }

    @Test
    @Story("Sing up")
    @DisplayName("Registration new user")
    fun testRegistration() = runBlocking {
        container.use {
            val res = authRepo.register(Credentials("aaa", "bbb"))
            assertTrue(res)
        }
    }

    @Test
    @Story("Sing up")
    @DisplayName("Registration with existing login")
    fun testRegistrationWithSameLogin() = runBlocking {
        container.use {
            val credentials = Credentials("login", "pass")
            val differentPass = Credentials(credentials.login, credentials.password + "suffix")
            assertTrue(authRepo.register(credentials))
            assertFalse(authRepo.register(credentials))
            assertFalse(authRepo.register(differentPass))
        }
    }

    @Test
    @Story("Sing in")
    @DisplayName("Login after registration")
    fun testLoginAfterRegistration() = runBlocking {
        container.use {
            val credentials = Credentials("login", "pass")
            assertTrue(authRepo.register(credentials))
            assertTrue(authRepo.login(credentials))
        }
    }

    @Test
    @Story("Sing in")
    @DisplayName("Login with wrong password")
    fun testLoginWithWrongPassword() = runBlocking {
        container.use {
            val credentials = Credentials("login", "pass")
            val wrongPass = Credentials(credentials.login, credentials.password + "suffix")
            assertTrue(authRepo.register(credentials))
            assertFalse(authRepo.login(wrongPass))
        }
    }

    @Test
    @Story("Sing in")
    @DisplayName("Login with non-existing credentials")
    fun testAbsentUser() = runBlocking {
        container.use {
            val credentials = Credentials("login", "password")
            assertFalse(authRepo.login(credentials))
        }
    }
}
