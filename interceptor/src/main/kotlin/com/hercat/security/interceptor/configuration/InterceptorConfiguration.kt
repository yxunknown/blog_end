package com.hercat.security.interceptor.configuration

import com.hercat.security.interceptor.interceptor.RequestLogInterceptor
import com.hercat.security.interceptor.interceptor.SecurityInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class InterceptorConfiguration: WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(SecurityInterceptor()).excludePathPatterns("**/error")
        registry.addInterceptor(RequestLogInterceptor()).addPathPatterns("/**")
    }
}