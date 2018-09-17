package com.hercats.com.photoprovider

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@SpringBootApplication
@EnableEurekaClient
//@ComponentScan(value = ["com.hercats.dev"])
@MapperScan(value = ["com.hercats.dev.commonbase.mapper"])
class PhotoProviderApplication

fun main(args: Array<String>) {
    runApplication<PhotoProviderApplication>(*args)
}
