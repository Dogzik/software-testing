package com.example.server.utils

import org.springframework.stereotype.Component
import java.util.*

@Component
class UUIDGeneratorImpl : UUIDGenerator {
    override fun genUUID(): String = UUID.randomUUID().toString()
}
