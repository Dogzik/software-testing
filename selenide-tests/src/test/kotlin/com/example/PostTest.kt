package com.example

import com.codeborne.selenide.CollectionCondition.size
import com.codeborne.selenide.Condition.*
import com.codeborne.selenide.Selectors.byText
import com.codeborne.selenide.Selenide.*
import com.codeborne.selenide.SelenideElement
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import io.qameta.allure.Story
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@Epic("Selenide tests")
@Feature("Post tests")
class PostTest : BaseTest() {
    private lateinit var login: String
    private lateinit var password: String

    @BeforeEach
    private fun startApp() {
        login = randomString(15)
        password = randomString(15)
        open("/")
        signUp(login, password)
    }

    private data class EmailListItem(val address: String, val subject: String)
    private data class EmailDetails(val address: String, val subject: String, val text: String)

    private companion object {
        fun toEmailListItem(elem: SelenideElement) = EmailListItem(
            elem.find(".EmailListItem__address").text,
            elem.find(".EmailListItem__subject").text
        )
    }

    private fun sendEmail(address: String, subject: String, text: String) {
        val writeButton = element(byText("Write"))
        writeButton.click()
        val inputs = elements("input")
        val addressInput = inputs[0]
        val subjectInput = inputs[1]
        addressInput.shouldHave(attribute("placeholder", "To"))
        subjectInput.shouldHave(attribute("placeholder", "Subject"))
        val textInput = element("textarea")
        addressInput.value = address
        subjectInput.value = subject
        textInput.value = text
        val sendButton = element(".EmailEditor__send")
        sendButton.click()
        sendButton.shouldHave(disappear)
    }

    private fun selectMailbox(name: String) {
        element(byText(name)).click()
        sleep(300)
    }

    private fun getSelectedEmail(): EmailDetails = EmailDetails(
        element(".EmailDetails__address").text,
        element(".EmailDetails__subject").text,
        element(".EmailDetails__text").text
    )

    private fun selectInbox() = selectMailbox("Inbox")
    private fun selectSent() = selectMailbox("Sent")

    @Test
    @Story("Mailboxes")
    @DisplayName("New user has empty mailboxes")
    fun emptyNewUser() {
        sleep(300)
        val inboxList = element(".EmailList")
        inboxList.shouldHave(text("Nothing to see here"))
        selectSent()
        val sentList = element(".EmailList")
        sentList.shouldHave(text("Nothing to see here"))
    }

    @Test
    @Story("Sending email")
    @DisplayName("New user sends email")
    fun newUserSendsEmail() {
        val address = randomString(8)
        val subject = randomString(15)
        val text = randomString(40)
        sendEmail(address, subject, text)
        selectSent()
        val letters = elements(".EmailListItem")
        letters.shouldHave(size(1))
        assertEquals(EmailListItem(address, subject), toEmailListItem(letters.first()))
        letters.first().click()
        assertEquals(EmailDetails(address, subject, text), getSelectedEmail())
    }

    @Test
    @Story("Mailboxes")
    @DisplayName("Login in to non-empty mailbox")
    fun nonEmptyMailBox() {
        refresh()
        val users = (0..1).map { mapOf("user" to randomString(10), "password" to randomString(10)) }
        val emails = (0..3).map { mapOf("subject" to randomString(15), "text" to randomString(20)) }

        val sendEmails = { dst: String, data: List<Map<String, String>> ->
            data.forEach {
                sendEmail(dst, it["subject"]!!, it["text"]!!)
                sleep(1000)
            }
        }

        signUp(users[0]["user"]!!, users[0]["password"]!!)
        sendEmails(users[1]["user"]!!, listOf(emails[0], emails[1]))
        refresh()

        signUp(users[1]["user"]!!, users[1]["password"]!!)
        sendEmails(users[0]["user"]!!, listOf(emails[2], emails[3]))
        refresh()

        signIn(users[0]["user"]!!, users[0]["password"]!!)
        sleep(300)
        val checkMailbox = { expectedEmails: List<Map<String, String>> ->
            val expectedLetters = expectedEmails.map { EmailListItem(users[1]["user"]!!, it["subject"]!!) }
            val letters = elements(".EmailListItem")
            assertEquals(expectedLetters, letters.map { toEmailListItem(it) })
            (0..1).forEach {
                letters[it].click()
                val expectedEmail = EmailDetails(
                    users[1]["user"]!!,
                    expectedEmails[it]["subject"]!!,
                    expectedEmails[it]["text"]!!
                )
                assertEquals(expectedEmail, getSelectedEmail())
            }
        }
        selectInbox()
        checkMailbox(listOf(emails[3], emails[2]))
        selectSent()
        checkMailbox(listOf(emails[1], emails[0]))
    }
}
