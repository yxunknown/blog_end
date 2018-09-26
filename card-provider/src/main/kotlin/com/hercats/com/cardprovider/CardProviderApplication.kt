package com.hercats.com.cardprovider

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@SpringBootApplication
@EnableEurekaClient
@MapperScan(value = ["com.hercats.dev.commonbase.mapper"])
class CardProviderApplication

fun main(args: Array<String>) {
    runApplication<CardProviderApplication>(*args)
}
