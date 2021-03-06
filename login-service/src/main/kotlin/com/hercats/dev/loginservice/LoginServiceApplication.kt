package com.hercats.dev.loginservice

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@EnableEurekaClient
@ComponentScan(value = ["com.hercats.dev.*", "com.hercat.security.*"])
@MapperScan(value = ["com.hercats.dev.commonbase.mapper"])
class LoginServiceApplication

fun main(args: Array<String>) {
    runApplication<LoginServiceApplication>(*args)
}
