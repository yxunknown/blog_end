package com.hercats.com.cardprovider

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@EnableEurekaClient
@ComponentScan(value = ["com.hercat.security.*", "com.hercats.com.*"])
@MapperScan(value = ["com.hercats.dev.commonbase.mapper"])
class CardProviderApplication

fun main(args: Array<String>) {
    runApplication<CardProviderApplication>(*args)
}
