package com.example.server.config

import com.typesafe.config.Config

interface ConfigProvider {
    val config: Config
}
