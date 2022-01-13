package com.example.server.repo

import com.example.server.model.Credentials
import com.github.jasync.sql.db.QueryResult
import org.springframework.stereotype.Component


@Component
class PostgresAuthenticationRepo(private val connectionProvider: ConnectionProvider) : AuthenticationRepo {
    companion object {
        val loginQuery = """
            SELECT login
            FROM Users
            WHERE login = ?
              AND pass_hash = crypt(?, pass_hash);
        """.trimIndent()

        val registerQuery = """
            INSERT INTO Users (login, pass_hash)
            VALUES (?, crypt(?, gen_salt('bf', 8)))
            ON CONFLICT (login) DO NOTHING;
        """.trimIndent()
    }

    private suspend fun sendAuthQuery(query: String, credentials: Credentials): QueryResult {
        val params = listOf(credentials.login, credentials.password)
        return connectionProvider.connection.sendPreparedStatement(query, params)
    }

    override suspend fun login(credentials: Credentials): Boolean {
        val queryResult = sendAuthQuery(loginQuery, credentials)
        return !queryResult.rows.isEmpty()
    }

    override suspend fun register(credentials: Credentials): Boolean {
        val queryResult = sendAuthQuery(registerQuery, credentials)
        return queryResult.rowsAffected > 0
    }
}
