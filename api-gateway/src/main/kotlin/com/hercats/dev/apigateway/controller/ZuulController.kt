package com.hercats.dev.apigateway.controller

import com.hercats.dev.commonbase.model.Message
import com.hercats.dev.commonbase.tool.ok
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class ZuulController {

    @RequestMapping(value = ["/hello", "/hello/"], method = [RequestMethod.GET])
    fun hello(): Message {
        val message = Message()
        message ok "hello"
        return message
    }
}