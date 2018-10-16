package com.hercat.security.interceptor

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class InterceptorApplication

fun main(args: Array<String>) {
    runApplication<InterceptorApplication>(*args)
}
