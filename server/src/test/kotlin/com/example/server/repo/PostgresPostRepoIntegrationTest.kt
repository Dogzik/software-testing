package com.example.server.repo

import com.example.server.config.DatabaseConfig
import com.example.server.model.Email
import com.example.server.utils.UUIDPool
import com.example.server.utils.getContainerWithConfig
import com.example.server.utils.randomEmail
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testcontainers.containers.PostgreSQLContainer

class PostgresPostRepoIntegrationTest {
    private lateinit var config: DatabaseConfig
    private lateinit var container: PostgreSQLContainer<*>
    private lateinit var postRepo: PostRepo
    private lateinit var idPool: UUIDPool

    @BeforeEach
    fun initContainers() = runBlocking {
        val (newConfig, newContainer) = getContainerWithConfig()
        config = newConfig
        container = newContainer
        postRepo = PostgresPostRepo(PostgresConnectionProvider(config))
        idPool = UUIDPool.getPool()
    }

    private fun randomEmailWithId() = randomEmail().copy(id = idPool.getId())

    private fun randomEmailWithUsers(src: String, dst: String) = randomEmailWithId().copy(from = src, to = dst)

    @Test
    fun testSendingEmail() = runBlocking {
        container.use {
            val email = randomEmailWithId()
            postRepo.addEmail(email)
            val connection = PostgresConnectionProvider(config).connection
            val query = """
                SELECT email_id::TEXT,
                   src,
                   dst,
                   subject,
                   cast(extract(EPOCH FROM time) as BIGINT) as time,
                   email_text
                FROM Letters
            """.trimIndent()
            val dbEmails = connection.sendQuery(query).rows.map { PostgresPostRepo.rowToEmail(it) }
            assertEquals(listOf(email), dbEmails)
        }
    }

    @Test
    fun testGettingMailboxes() = runBlocking {
        val user1 = "kek@lol.com"
        val user2 = "lol@lel.com"
        val emailsFromUser1 = (1..7).map { randomEmailWithUsers(user1, user2) }
        val emailsFromUser2 = (1..8).map { randomEmailWithUsers(user2, user1) }
        val sortedEmailsFromUser1 = emailsFromUser1.sortedBy(Email::time).asReversed()
        val sortedEmailsFromUser2 = emailsFromUser2.sortedBy(Email::time).asReversed()
        container.use {
            listOf(emailsFromUser1, emailsFromUser2).forEach { emails ->
                emails.forEach { postRepo.addEmail(it) }
            }
            assertEquals(sortedEmailsFromUser1, postRepo.getSent(user1))
            assertEquals(sortedEmailsFromUser2, postRepo.getInbox(user1))
            assertEquals(sortedEmailsFromUser2, postRepo.getSent(user2))
            assertEquals(sortedEmailsFromUser1, postRepo.getInbox(user2))
        }
    }
}
