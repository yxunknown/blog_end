package com.hercats.dev.articleprovider.controller

import com.hercats.dev.commonbase.mapper.ArticleMapper
import com.hercats.dev.commonbase.model.Article
import com.hercats.dev.commonbase.model.Message
import com.hercats.dev.commonbase.model.SqlDate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class ArticleController(@Autowired val articleMapper: ArticleMapper) {

    @RequestMapping(value = ["/hello", "/hello/"], method = [RequestMethod.GET])
    fun hello() = "hello"

    @RequestMapping(value = ["/article", "/article/"], method = [RequestMethod.POST])
    fun addArticle(article: Article): Message {
        val msg = Message()
        if (validateArticle(article, msg)) {
            try {
                article.datetime = SqlDate()
                if (articleMapper.insert(article) == 1) {
                    msg.info = "添加文章成功"
                    msg.map("article", article)
                } else {
                    msg.info = "添加文章失败"
                    msg.code = 500
                }
            } catch (e: Exception) {
                msg.code = 500
                msg.info = e.message ?: ""
            }
        } else {
            msg.code = 500
        }
        return msg
    }

    fun validateArticle(article: Article, msg: Message): Boolean {
        var result = true
        if (article.author.account.isBlank()) {
            msg.info += "作者邮箱为空"
            result = false
        }
        if (article.title.isBlank()) {
            msg.info += "文章标题为空"
            result = false
        }
        if (article.catalog.id == -1) {
            msg.info += "分类为空"
            result = false
        }
        return result

    }
}