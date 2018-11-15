package com.hercats.com.cardprovider.controller

import com.hercats.dev.commonbase.mapper.CardMapper
import com.hercats.dev.commonbase.mapper.PhotoMapper
import com.hercats.dev.commonbase.mapper.UserMapper
import com.hercats.dev.commonbase.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import com.hercats.dev.commonbase.tool.*

@RestController
class CardController(@Autowired val cardMapper: CardMapper,
                     @Autowired val userMapper: UserMapper,
                     @Autowired val photoMapper: PhotoMapper) {

    @RequestMapping(value = ["/hello", "/hello/"], method = [RequestMethod.GET])
    fun hello() = Message().apply { ok("hello") }

    @RequestMapping(value = ["/card", "/card/"], method = [RequestMethod.POST])
    fun addCard(card: Card): Message {
        val msg = Message()
        card.datetime = SqlDate().toString()
        when {
            (card.author.account be blank) -> {
                msg error_400 "作者不能为空"
            }
            (userMapper.selectByPrimaryKey(card.author.account) == null) -> {
                msg error_400 "作者用户信息不存在"
            }
            (card.content be blank) -> {
                msg error_400 "内容不能为空"
            }
            else -> {
                try {
                    if (cardMapper.insert(card) == 1) {
                        msg ok "添加新留言成功"
                        msg.map("card", cardMapper.selectByPrimaryKey(card.id) ?: "")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    msg error_500 (e.message ?: "未知错误")
                }
            }
        }
        return msg
    }

    @RequestMapping(value = ["/card", "/card/"], method = [RequestMethod.GET])
    fun getCard(pagination: Pagination): Message {
        val msg = Message()
        try {
            val cards = cardMapper.select(pagination)
            msg ok "查询成功"
            msg.map("count", cardMapper.count())
            msg.map("pagination", pagination)
            msg.map("cards", cards.groupBy { it.datetime.substringBeforeLast(" ")}
                    .map { BlackCard(it.key, it.value) }
                    .toList())
        } catch (e: Exception) {
            e.printStackTrace()
            msg.error_500(e.message ?: "未知错误")
        }
        return msg

    }

    @RequestMapping(value = ["/card", "/card/"], method = [RequestMethod.PUT])
    fun update(card: Card): Message {
        val msg = Message()
        when {
            (card.id == -1) -> {
                msg error_400 "缺少id参数"
            }
            else -> {
                try {
                    if (cardMapper.update(card) == 1) {
                        msg ok "更新成功"
                        msg.map("card", cardMapper.selectByPrimaryKey(card.id) ?: "")
                    } else {
                        msg error_500 "更新失败"
                    }
                } catch (e: Exception) {
                    msg error_500 (e.message ?: "未知错误")
                }
            }
        }
        return msg
    }

}
