package com.example.server.configuration

import com.example.server.service.AuthenticationService
import com.example.server.service.PostService
import io.mockk.mockk
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
@ConditionalOnProperty(value = ["test.serviceTestConfiguration"], havingValue = "mockk", matchIfMissing = true)
class MockkServiceTestConfiguration : ServiceTestConfiguration {
    @Bean
    override fun getAuthenticationService(): AuthenticationService = mockk()

    @Bean
    override fun getPostService(): PostService = mockk()
}
