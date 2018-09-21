package com.hercats.com.cardprovider.controller

import com.hercats.dev.commonbase.mapper.CardMapper
import com.hercats.dev.commonbase.mapper.UserMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import com.hercats.dev.commonbase.model.Message
import com.hercats.dev.commonbase.tool.ok

@RestController
class CardController(@Autowired val cardMapper: CardMapper,
                     @Autowired val userMapper: UserMapper) {

    @RequestMapping(value = ["/hello", "/hello/"], method = [RequestMethod.GET])
    fun hello() = Message().apply { ok("hello") }

}
