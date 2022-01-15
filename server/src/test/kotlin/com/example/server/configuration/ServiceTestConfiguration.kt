package com.example.server.configuration

import com.example.server.service.AuthenticationService
import com.example.server.service.PostService
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

interface ServiceTestConfiguration : WebMvcConfigurer {
    @Bean
    fun getAuthenticationService(): AuthenticationService

    @Bean
    fun getPostService(): PostService
}
