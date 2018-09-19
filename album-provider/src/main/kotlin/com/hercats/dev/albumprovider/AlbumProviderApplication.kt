package com.hercats.dev.albumprovider

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@SpringBootApplication
@EnableEurekaClient
@MapperScan(value = ["com.hercats.dev.commonbase.mapper"])
class AlbumProviderApplication

fun main(args: Array<String>) {
    runApplication<AlbumProviderApplication>(*args)
}
