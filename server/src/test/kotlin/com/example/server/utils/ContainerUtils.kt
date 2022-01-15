package com.example.server.utils

import com.example.server.config.DatabaseConfig
import com.github.jasync.sql.db.asSuspending
import com.github.jasync.sql.db.postgresql.PostgreSQLConnectionBuilder
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.containers.wait.strategy.WaitAllStrategy
import java.nio.file.Files
import java.nio.file.Paths
import java.time.Duration

private val WAIT_STRATEGY = WaitAllStrategy(WaitAllStrategy.Mode.WITH_OUTER_TIMEOUT)
    .withStrategy(Wait.forListeningPort())
    .withStrategy(Wait.forLogMessage(".*database system is ready to accept connections.*", 1))
    .withStartupTimeout(Duration.ofSeconds(40))

@Suppress("SameParameterValue")
private fun startContainer(user: String, password: String, database: String, exposedPort: Int): PostgreSQLContainer<*> {
    val container = PostgreSQLContainer<Nothing>("postgres:latest").apply {
        withDatabaseName(database)
        withUsername(user)
        withPassword(password)
        withExposedPorts(exposedPort)
        waitingFor(WAIT_STRATEGY)
    }
    container.start()
    return container
}

suspend fun getContainerWithConfig(): Pair<DatabaseConfig, PostgreSQLContainer<*>> {
    val user = "user"
    val pass = "password"
    val db = "test_db"
    val exposedPort = 5432
    val container = startContainer(user, pass, db, exposedPort)

    try {
        val connection = PostgreSQLConnectionBuilder.createConnectionPool {
            host = container.host
            port = container.getMappedPort(exposedPort)
            database = db
            username = user
            password = pass
            maxActiveConnections = 7
        }.asSuspending
        @Suppress("BlockingMethodInNonBlockingContext")
        Files.newBufferedReader(Paths.get("scripts/create-tables.sql")).use { reader ->
            reader.readText()
                .split(";")
                .dropLast(1)
                .forEach { connection.sendQuery("$it;") }
        }
        val config = object : DatabaseConfig {
            override val host = container.host
            override val port = container.getMappedPort(exposedPort)
            override val database = db
            override val username = user
            override val password = pass
            override val maxActiveConnections = 32
        }
        return Pair(config, container)
    } catch (e: Throwable) {
        try {
            container.close()
        } catch (innerE: Throwable) {
            e.addSuppressed(innerE)
        }
        throw e
    }
}
