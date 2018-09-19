package com.hercats.dev.loginservice.controller

import com.hercats.dev.commonbase.mapper.UserMapper
import com.hercats.dev.commonbase.model.Message
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController(@Autowired val userMapper: UserMapper) {

    @RequestMapping(value = ["/hello", "/hello/"], method = [RequestMethod.GET])
    fun hello(): Message {
        val msg = Message()
        msg.code = 200
        msg.info = "hello"
        return msg
    }
}