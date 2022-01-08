package com.example.server.controller

import com.example.server.model.Email
import com.example.server.model.IncompleteEmail
import com.example.server.service.PostService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class PostController(private val postService: PostService) {
    @GetMapping(
        path = ["/get/inbox"],
        produces = ["application/json"]
    )
    suspend fun getInbox(@RequestParam user: String): ResponseEntity<List<Email>> =
        ResponseEntity.status(HttpStatus.OK).body(postService.getInbox(user))

    @GetMapping(
        path = ["/get/sent"],
        produces = ["application/json"]
    )
    suspend fun getSent(@RequestParam user: String): ResponseEntity<List<Email>> =
        ResponseEntity.status(HttpStatus.OK).body(postService.getSent(user))

    @PostMapping(
        path = ["/send_email"],
        consumes = ["application/json"],
        produces = ["application/json"]
    )
    suspend fun addEmail(@RequestBody incompleteEmail: IncompleteEmail): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.OK).body(postService.addEmail(incompleteEmail))
}
