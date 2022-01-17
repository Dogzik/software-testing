package com.example

private val charPool = ('a'..'z') + ('A'..'Z') + ('0'..'9')

fun randomString(length: Int) = (1..length)
    .map { kotlin.random.Random.nextInt(0, charPool.size) }
    .map(charPool::get)
    .joinToString("")
