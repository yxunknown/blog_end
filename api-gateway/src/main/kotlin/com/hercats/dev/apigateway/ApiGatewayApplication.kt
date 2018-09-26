package com.hercats.dev.apigateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.cloud.netflix.zuul.EnableZuulProxy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.web.client.RestTemplate

@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
@ComponentScan(value = ["com.hercats.dev"])
class ApiGatewayApplication {
    @Bean
    @LoadBalanced
    fun restTemplate() = RestTemplate()
}

fun main(args: Array<String>) {
    runApplication<ApiGatewayApplication>(*args)
}

