package com.hercat.security.interceptor.configuration.log

import com.hercat.security.interceptor.interceptor.RequestLogInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class RequestLogConfiguration: WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(RequestLogInterceptor()).addPathPatterns("/**")
    }
}