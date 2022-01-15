package com.example.server.service

import com.example.server.model.Email
import com.example.server.model.IncompleteEmail
import com.example.server.repo.PostRepo
import com.example.server.utils.TimeService
import com.example.server.utils.UUIDGenerator
import com.example.server.utils.randomEmail
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DBPostServiceTest {
    private val mockPostRepo = mockk<PostRepo>()
    private val mockTime = mockk<TimeService>()
    private val mockIdGenerator = mockk<UUIDGenerator>()
    private val postService = DBPostService(mockPostRepo, mockIdGenerator, mockTime)

    @BeforeEach
    fun clearTestMocks() {
        clearMocks(mockPostRepo, mockTime, mockIdGenerator)
    }

    @Test
    fun testSendingEmail() = runBlocking {
        val testData = IncompleteEmail("from", "to", "subject", "text")
        val testTime = 159814351L
        val testId = "id"
        coEvery { mockPostRepo.addEmail(any()) } just Runs
        every { mockTime.currentTimestamp() } returns testTime
        every { mockIdGenerator.genUUID() } returns testId
        postService.addEmail(testData)
        val expectedEmail = Email(testData, testId, testTime)
        coVerify(exactly = 1) {
            mockIdGenerator.genUUID()
            mockTime.currentTimestamp()
            mockPostRepo.addEmail(expectedEmail)
        }
    }


    private fun testGettingMailbox(
        serviceAction: suspend PostService.(String) -> List<Email>,
        repoAction: suspend PostRepo.(String) -> List<Email>
    ) = runBlocking {
        val user1 = "kek"
        val user2 = "lol"
        val mailbox1 = (1..10).map { randomEmail() }
        val mailbox2 = (1..12).map { randomEmail() }
        coEvery { mockPostRepo.repoAction(user1) } returns mailbox1
        coEvery { mockPostRepo.repoAction(user2) } returns mailbox2
        assertEquals(mailbox1, postService.serviceAction(user1))
        assertEquals(mailbox2, postService.serviceAction(user2))
        coVerify(exactly = 1) {
            mockPostRepo.repoAction(user1)
            mockPostRepo.repoAction(user2)
        }
    }

    @Test
    fun testGettingInbox() = testGettingMailbox({ getInbox(it) }) { getInbox(it) }


    @Test
    fun testGettingSent() = testGettingMailbox({ getSent(it) }) { getSent(it) }
}
