package com.hercats.dev.commonbase

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CommonBaseApplication

fun main(args: Array<String>) {
    runApplication<CommonBaseApplication>(*args)
}
