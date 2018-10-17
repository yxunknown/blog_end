package com.hercats.dev.commonbase.configuration

import com.hercats.dev.commonbase.interceptor.RequestLogInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class RequestLogConfiguration: WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        println("RequestLogInterceptor init success")
        registry.addInterceptor(RequestLogInterceptor()).addPathPatterns("/**")
    }
}