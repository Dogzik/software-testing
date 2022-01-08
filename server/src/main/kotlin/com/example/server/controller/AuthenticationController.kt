package com.example.server.controller

import com.example.server.model.Credentials
import com.example.server.service.AuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthenticationController(@Autowired private val authenticationService: AuthenticationService) {
    @PostMapping(
        path = ["/login"],
        consumes = ["application/json"],
        produces = ["application/json"]
    )
    suspend fun login(@RequestBody credentials: Credentials): ResponseEntity<String> =
        if (authenticationService.login(credentials))
            ResponseEntity.status(HttpStatus.OK).body("Success")
        else
            ResponseEntity.status(HttpStatus.FORBIDDEN).body("Incorrect credentials")

    @PostMapping(
        path = ["/register"],
        consumes = ["application/json"],
        produces = ["application/json"]
    )
    suspend fun register(@RequestBody credentials: Credentials): ResponseEntity<String> =
        if (authenticationService.register(credentials))
            ResponseEntity.status(HttpStatus.OK).body("Success")
        else
            ResponseEntity.status(HttpStatus.CONFLICT).body("User ${credentials.login} already exists")
}
