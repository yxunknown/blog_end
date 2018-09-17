package com.hercats.com.photoprovider.controller

import com.hercats.dev.commonbase.model.Message
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class PhotoController {

    @RequestMapping(value = ["/hello", "/hello/"], method = [RequestMethod.GET])
    fun hello(): Message {
        val msg = Message()
        msg.info = "hello"
        return msg
    }

    @RequestMapping(value = ["/test", "/test/"], method = [RequestMethod.GET])
    fun test() = "test"
}