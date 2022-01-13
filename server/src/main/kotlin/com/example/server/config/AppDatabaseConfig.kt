package com.example.server.config

import org.springframework.stereotype.Component

@Component
class AppDatabaseConfig(configProvider: ConfigProvider) : DatabaseConfig {
    private val config = configProvider.config

    override val host: String
        get() = config.getString("host")
    override val port: Int
        get() = config.getInt("port")
    override val database: String
        get() = config.getString("database")
    override val username: String
        get() = config.getString("username")
    override val password: String
        get() = config.getString("password")
    override val maxActiveConnections: Int
        get() = config.getInt("maxActiveConnections")
}
