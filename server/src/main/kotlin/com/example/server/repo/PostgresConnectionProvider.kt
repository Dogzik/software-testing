package com.example.server.repo

import com.example.server.config.DatabaseConfig
import com.github.jasync.sql.db.SuspendingConnection
import com.github.jasync.sql.db.asSuspending
import com.github.jasync.sql.db.postgresql.PostgreSQLConnectionBuilder
import org.springframework.stereotype.Component

@Component
class PostgresConnectionProvider(config: DatabaseConfig) : ConnectionProvider {
    private val pool = PostgreSQLConnectionBuilder.createConnectionPool {
        host = config.host
        port = config.port
        database = config.database
        username = config.username
        password = config.password
        maxActiveConnections = config.maxActiveConnections
    }

    override val connection: SuspendingConnection
        get() = pool.asSuspending
}
