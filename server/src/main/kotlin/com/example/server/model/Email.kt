package com.example.server.model

data class Email(
    val id: String,
    val from: String,
    val to: String,
    val time: Long,
    val subject: String,
    val text: String
) {
    constructor(incompleteEmail: IncompleteEmail, id: String, time: Long) : this(
        id,
        incompleteEmail.from,
        incompleteEmail.to,
        time,
        incompleteEmail.subject,
        incompleteEmail.text
    )

    override fun equals(other: Any?): Boolean =
        if (other is Email)
            id == other.id
        else
            false

    override fun hashCode(): Int = id.hashCode()
}
