package com.example.server.repo

import com.github.jasync.sql.db.SuspendingConnection

interface ConnectionProvider {
    val connection: SuspendingConnection
}
