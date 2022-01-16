package com.example.server.repo

import com.example.server.model.Email
import com.example.server.utils.randomEmail
import com.github.jasync.sql.db.ResultSet
import com.github.jasync.sql.db.RowData
import com.github.jasync.sql.db.SuspendingConnection
import io.mockk.*
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import io.qameta.allure.Story
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@Epic("Repo tests")
@Feature("Post tests")
class PostgresPostRepoTest {
    private val mockConnectionProvider = mockk<ConnectionProvider>()
    private val postRepo = PostgresPostRepo(mockConnectionProvider)

    @BeforeEach
    private fun clearTestMocks() {
        clearMocks(mockConnectionProvider)
    }

    @Test
    @Story("Sending email")
    @DisplayName("Sending single email")
    fun testSendingEmail() = runBlocking {
        val email = randomEmail()
        val params = listOf(email.id, email.from, email.to, email.subject, email.time, email.text)
        val mockConnection = mockk<SuspendingConnection> {
            coEvery { sendPreparedStatement(any(), any()) } answers {
                mockk {
                    every { rowsAffected } returns 1
                }
            }
        }
        every { mockConnectionProvider.connection } returns mockConnection
        postRepo.addEmail(email)
        coVerify(exactly = 1) { mockConnectionProvider.connection }
        coVerify(exactly = 1) {
            mockConnection.sendPreparedStatement(PostgresPostRepo.sendEmailQuery, params)
        }
    }

    class FakeResultSet(private val fakeRaws: List<RowData>) : ResultSet, List<RowData> by fakeRaws {
        override fun columnNames() = emptyList<String>()
    }

    private companion object {
        private fun mockRawData(email: Email): RowData = mockk {
            every { getString("email_id") } returns email.id
            every { getString("src") } returns email.from
            every { getString("dst") } returns email.to
            every { getLong("time") } returns email.time
            every { getString("subject") } returns email.subject
            every { getString("email_text") } returns email.text
        }

        private fun mockMailboxConnection(emails: List<Email>): SuspendingConnection = mockk {
            coEvery { sendPreparedStatement(any(), any()) } answers {
                mockk {
                    every { rows } returns FakeResultSet(emails.map { mockRawData(it) })
                }
            }
        }


    }

    private fun testGettingMailbox(query: String, postAction: suspend PostRepo.(String) -> List<Email>) = runBlocking {
        val user1 = "kek"
        val user2 = "lol"
        val mailbox1 = (1..10).map { randomEmail() }
        val mailbox2 = (1..12).map { randomEmail() }
        val connection1 = mockMailboxConnection(mailbox1)
        val connection2 = mockMailboxConnection(mailbox2)
        coEvery { mockConnectionProvider.connection } returns connection1 andThen connection2
        assertEquals(mailbox1, postRepo.postAction(user1))
        assertEquals(mailbox2, postRepo.postAction(user2))
        coVerify(exactly = 2) { mockConnectionProvider.connection }
        coVerify(exactly = 1) {
            connection1.sendPreparedStatement(query, listOf(user1))
        }
        coVerify(exactly = 1) {
            connection2.sendPreparedStatement(query, listOf(user2))
        }
    }

    @Test
    @Story("Getting mailbox")
    @DisplayName("Getting inbox")
    fun testGettingInbox() = testGettingMailbox(PostgresPostRepo.getInboxQuery) { getInbox(it) }


    @Test
    @Story("Getting mailbox")
    @DisplayName("Getting sent")
    fun testGettingSent() = testGettingMailbox(PostgresPostRepo.getSentQuery) { getSent(it) }
}
