package com.example.server.service

import com.example.server.model.Email
import com.example.server.model.IncompleteEmail
import com.example.server.repo.PostRepo
import com.example.server.utils.TimeService
import com.example.server.utils.UUIDGenerator
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component

@Component
@Primary
class DBPostService(
    private val postRepo: PostRepo,
    private val idGenerator: UUIDGenerator,
    private val timeService: TimeService
) : PostService {
    override suspend fun getInbox(user: String): List<Email> = postRepo.getInbox(user)

    override suspend fun getSent(user: String): List<Email> = postRepo.getSent(user)

    override suspend fun addEmail(incompleteEmail: IncompleteEmail): String {
        val id = idGenerator.genUUID()
        val time = timeService.currentTimestamp()
        postRepo.addEmail(Email(incompleteEmail, id, time))
        return id
    }
}
