package com.example.server.repo

import com.example.server.model.Credentials

interface AuthenticationRepo {
    suspend fun login(credentials: Credentials): Boolean
    suspend fun register(credentials: Credentials): Boolean
}
