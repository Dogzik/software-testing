package com.example.server.configuration

import com.example.server.service.AuthenticationService
import com.example.server.service.PostService
import com.ninjasquad.springmockk.MockkBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
@ConditionalOnProperty(value = ["test.serviceTestConfiguration"], havingValue = "springmockk")
class SpringMockkServiceTestConfiguration : ServiceTestConfiguration {
    @MockkBean
    private lateinit var mockAuthService: AuthenticationService

    @MockkBean
    private lateinit var mockPostService: PostService

    @Bean
    override fun getAuthenticationService(): AuthenticationService = mockAuthService

    @Bean
    override fun getPostService(): PostService = mockPostService
}
