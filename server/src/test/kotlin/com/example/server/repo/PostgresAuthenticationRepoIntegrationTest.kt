package com.example.server.repo

import com.example.server.config.DatabaseConfig
import com.example.server.model.Credentials
import com.example.server.utils.getContainerWithConfig
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.testcontainers.containers.PostgreSQLContainer

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
    fun testRegistration() = runBlocking {
        container.use {
            val res = authRepo.register(Credentials("aaa", "bbb"))
            assertTrue(res)
        }
    }

    @Test
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
    fun testLoginAfterRegistration() = runBlocking {
        container.use {
            val credentials = Credentials("login", "pass")
            assertTrue(authRepo.register(credentials))
            assertTrue(authRepo.login(credentials))
        }
    }

    @Test
    fun testLoginWithWrongPassword() = runBlocking {
        container.use {
            val credentials = Credentials("login", "pass")
            val wrongPass = Credentials(credentials.login, credentials.password + "suffix")
            assertTrue(authRepo.register(credentials))
            assertFalse(authRepo.login(wrongPass))
        }
    }

    @Test
    fun testAbsentUser() = runBlocking {
        container.use {
            val credentials = Credentials("login", "password")
            assertFalse(authRepo.login(credentials))
        }
    }
}
