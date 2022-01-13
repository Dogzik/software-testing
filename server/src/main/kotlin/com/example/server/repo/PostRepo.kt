package com.example.server.repo

import com.example.server.model.Email

interface PostRepo {
    suspend fun getInbox(user: String): List<Email>
    suspend fun getSent(user: String): List<Email>
    suspend fun addEmail(email: Email)
}
