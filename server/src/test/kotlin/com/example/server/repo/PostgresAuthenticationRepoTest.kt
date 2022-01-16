package com.example.server.repo

import com.example.server.model.Credentials
import com.github.jasync.sql.db.QueryResult
import com.github.jasync.sql.db.SuspendingConnection
import io.mockk.*
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import io.qameta.allure.Story
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@Epic("Repo tests")
@Feature("Authentication tests")
class PostgresAuthenticationRepoTest {
    private val mockedConnectionProvider = mockk<ConnectionProvider>()
    private val authRepo = PostgresAuthenticationRepo(mockedConnectionProvider)

    @BeforeEach
    fun clearTestMocks() {
        clearMocks(mockedConnectionProvider)
    }

    private fun getMockQueryResult(empty: Boolean, cntAffected: Long) = mockk<QueryResult> {
        every { rows } answers {
            mockk {
                every { isEmpty() } returns empty
            }
        }
        every { rowsAffected } returns cntAffected
    }

    private fun getMockedConnection(query: String, params: List<Any?>, empty: Boolean, cntAffected: Long) =
        mockk<SuspendingConnection> {
            coEvery { sendPreparedStatement(query, params) } returns getMockQueryResult(empty, cntAffected)
        }

    @Test
    @Story("Sign in")
    @DisplayName("Signing in with correct and incorrect credentials")
    fun testLogin() = runBlocking {
        val wrongParams = listOf("false", "false")
        val correctParams = listOf("true", "true")
        val loginQuery = PostgresAuthenticationRepo.loginQuery
        val falseConnection = getMockedConnection(loginQuery, wrongParams, true, 0)
        val trueConnection = getMockedConnection(loginQuery, correctParams, false, 0)
        every { mockedConnectionProvider.connection } returns falseConnection andThen trueConnection
        assertEquals(false, authRepo.login(Credentials(wrongParams[0], wrongParams[1])))
        assertEquals(true, authRepo.login(Credentials(correctParams[0], correctParams[1])))
        coVerify(exactly = 2) {
            mockedConnectionProvider.connection
        }
        coVerify(exactly = 1) {
            falseConnection.sendPreparedStatement(loginQuery, wrongParams)
            trueConnection.sendPreparedStatement(loginQuery, correctParams)
        }
    }

    @Test
    @Story("Sign up")
    @DisplayName("Signing up with same credentials twice")
    fun testRegister() = runBlocking {
        val testParams = listOf("aaa", "bbb")
        val testCredentials = Credentials(testParams[0], testParams[1])
        val registerQuery = PostgresAuthenticationRepo.registerQuery
        val trueConnection = getMockedConnection(registerQuery, testParams, true, 1)
        val falseConnection = getMockedConnection(registerQuery, testParams, true, 0)
        every { mockedConnectionProvider.connection } returns trueConnection andThen falseConnection
        assertEquals(true, authRepo.register(testCredentials))
        assertEquals(false, authRepo.register(testCredentials))
        coVerify(exactly = 2) {
            mockedConnectionProvider.connection
        }
        coVerify(exactly = 1) {
            trueConnection.sendPreparedStatement(registerQuery, testParams)
            falseConnection.sendPreparedStatement(registerQuery, testParams)
        }
    }
}
