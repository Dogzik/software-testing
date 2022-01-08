package com.example.server.utils

import org.springframework.stereotype.Component
import java.time.Instant

@Component
class InstantTimeService : TimeService {
    override fun currentTimestamp(): Long = Instant.now().epochSecond
}
