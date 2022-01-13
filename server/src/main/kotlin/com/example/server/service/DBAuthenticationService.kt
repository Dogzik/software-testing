package com.example.server.service

import com.example.server.model.Credentials
import com.example.server.repo.AuthenticationRepo
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

@Component
@Primary
class DBAuthenticationService(private val authenticationRepo: AuthenticationRepo) : AuthenticationService {
    override suspend fun login(credentials: Credentials): Boolean = authenticationRepo.login(credentials)

    override suspend fun register(credentials: Credentials): Boolean = authenticationRepo.register(credentials)
}
