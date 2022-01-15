package com.example.server.utils

import com.example.server.model.Email
import com.example.server.model.IncompleteEmail

private val charPool = ('a'..'z') + ('A'..'Z') + ('0'..'9')

fun randomString(length: Int) = (1..length)
    .map { kotlin.random.Random.nextInt(0, charPool.size) }
    .map(charPool::get)
    .joinToString("")

fun randomIncompleteEmail() = IncompleteEmail(
    randomString(10),
    randomString(10),
    randomString(15),
    randomString(25)
)

fun randomEmail() = Email(
    randomIncompleteEmail(),
    randomString(10),
    kotlin.random.Random.nextLong(0, 2000000000)
)
