package com.example.server.repo

import com.example.server.model.Email
import com.github.jasync.sql.db.RowData
import org.springframework.stereotype.Component

@Component
class PostgresPostRepo(private val connectionProvider: ConnectionProvider) : PostRepo {
    companion object {
        val getInboxQuery = """
            SELECT email_id::TEXT,
                   src,
                   dst,
                   subject,
                   cast(extract(EPOCH FROM time) as BIGINT) as time,
                   email_text
            FROM Letters
            WHERE dst = ?
            ORDER BY time DESC;
        """.trimIndent()

        val getSentQuery = """
            SELECT email_id::TEXT,
                   src,
                   dst,
                   subject,
                   cast(extract(EPOCH FROM time) as BIGINT) as time,
                   email_text
            FROM Letters
            WHERE src = ?
            ORDER BY time DESC;
        """.trimIndent()

        val sendEmailQuery = """
            INSERT INTO Letters (email_id, src, dst, subject, time, email_text)
            VALUES (?, ?, ?, ?, to_timestamp(?), ?)
            ON CONFLICT DO NOTHING;
        """.trimIndent()

        fun rowToEmail(row: RowData) = Email(
            row.getString("email_id")!!,
            row.getString("src")!!,
            row.getString("dst")!!,
            row.getLong("time")!!,
            row.getString("subject")!!,
            row.getString("email_text")!!
        )
    }

    private suspend fun getMailbox(query: String, user: String): List<Email> {
        val queryResult = connectionProvider.connection.sendPreparedStatement(query, listOf(user))
        return queryResult.rows.map { rowToEmail(it) }
    }

    override suspend fun getInbox(user: String): List<Email> = getMailbox(getInboxQuery, user)

    override suspend fun getSent(user: String): List<Email> = getMailbox(getSentQuery, user)

    override suspend fun addEmail(email: Email) {
        val params = listOf(email.id, email.from, email.to, email.subject, email.time, email.text)
        val queryResult = connectionProvider.connection.sendPreparedStatement(sendEmailQuery, params)
        assert(queryResult.rowsAffected > 0) { "Failed to send message" }
    }
}
