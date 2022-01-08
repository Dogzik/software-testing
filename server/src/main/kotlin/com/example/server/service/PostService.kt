package com.example.server.service

import com.example.server.model.Email
import com.example.server.model.IncompleteEmail

interface PostService {
    suspend fun getInbox(user: String): List<Email>
    suspend fun getSent(user: String): List<Email>
    suspend fun addEmail(incompleteEmail: IncompleteEmail): String
}
