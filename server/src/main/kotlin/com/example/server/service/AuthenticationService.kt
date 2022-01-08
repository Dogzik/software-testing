package com.example.server.service

import com.example.server.model.Credentials

interface AuthenticationService {
    suspend fun login(credentials: Credentials): Boolean
    suspend fun register(credentials: Credentials): Boolean
}
