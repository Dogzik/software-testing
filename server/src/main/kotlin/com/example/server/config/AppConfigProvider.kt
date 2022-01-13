package com.example.server.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.file.Paths

@Component
class AppConfigProvider(@Value("\${config.path}") private val configPath: String) : ConfigProvider {
    private val realConfig = ConfigFactory.parseFile(Paths.get(configPath).toFile())

    override val config: Config
        get() = realConfig
}
