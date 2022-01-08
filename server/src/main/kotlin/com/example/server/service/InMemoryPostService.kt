package com.example.server.service

import com.example.server.model.Email
import com.example.server.model.IncompleteEmail
import com.example.server.utils.TimeService
import com.example.server.utils.UUIDGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

@Component
class InMemoryPostService(private val idGenerator: UUIDGenerator, private val timeService: TimeService) : PostService {
    private val emails = ConcurrentHashMap.newKeySet<Email>()

    override suspend fun getInbox(user: String): List<Email> =
        emails.filter { it.to == user }.sortedBy(Email::time).reversed()

    override suspend fun getSent(user: String): List<Email> =
        emails.filter { it.from == user }.sortedBy(Email::time).reversed()

    override suspend fun addEmail(incompleteEmail: IncompleteEmail): String {
        val id = idGenerator.genUUID()
        val time = timeService.currentTimestamp()
        emails.add(Email(incompleteEmail, id, time))
        return id
    }
}
