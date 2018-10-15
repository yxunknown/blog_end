package com.hercats.dev.commonbase.configuration

import com.hercats.dev.commonbase.interceptor.RequestSecurityInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class RequestSecurityConfigure: WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(RequestSecurityInterceptor()).addPathPatterns("/**")
        super.addInterceptors(registry)
    }
}