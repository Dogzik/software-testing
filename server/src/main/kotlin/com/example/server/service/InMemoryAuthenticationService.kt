package com.example.server.service

import com.example.server.model.Credentials
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class InMemoryAuthenticationService : AuthenticationService {
    private val userCredentials = ConcurrentHashMap<String, String>()

    override suspend fun login(credentials: Credentials): Boolean =
        userCredentials[credentials.login] == credentials.password

    override suspend fun register(credentials: Credentials): Boolean =
        userCredentials.putIfAbsent(credentials.login, credentials.password) == null
}
