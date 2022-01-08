package com.example.server.model

data class IncompleteEmail(
    val from: String,
    val to: String,
    val subject: String,
    val text: String
)
