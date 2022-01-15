package com.example.server.model

import kotlinx.serialization.Serializable

@Serializable
data class Credentials(val login: String, val password: String)
