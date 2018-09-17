package com.hercats.dev.articleprovider.controller

import com.hercats.dev.commonbase.mapper.ArticleMapper
import com.hercats.dev.commonbase.model.Article
import com.hercats.dev.commonbase.model.Message
import com.hercats.dev.commonbase.model.Pagination
import com.hercats.dev.commonbase.model.SqlDate
import com.hercats.dev.commonbase.tool.getId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import kotlin.math.absoluteValue

@RestController
class ArticleController(@Autowired val articleMapper: ArticleMapper) {

    @RequestMapping(value = ["/hello", "/hello/"], method = [RequestMethod.GET])
    fun hello() = "hello"

    @RequestMapping(value = ["/article", "/article/"], method = [RequestMethod.POST])
    fun addArticle(article: Article): Message {
        val msg = Message()
        if (validateArticle(article, msg)) {
            try {
                article.id = getId().toInt().absoluteValue
                article.datetime = SqlDate().toString()
                if (articleMapper.insert(article) == 1) {
                    msg.info = "添加文章成功"
                    msg.map("article", articleMapper.selectByPrimaryKey(article.id) ?: "")
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

    @RequestMapping(value = ["/article/{id}", "/article/{id}/"], method = [RequestMethod.GET])
    fun getArticleById(@PathVariable("id") id: Long): Message {
        val msg = Message()
        try {
            val article = articleMapper.selectByPrimaryKey(id.toInt())
            if (article is Article) {
                msg.info = "查找成功"
                msg.map("article", article)
            } else {
                msg.code = 400
                msg.info = "文章不存在"
            }
        } catch (e: Exception) {
            msg.code = 500
            msg.info = e.message ?: "未知错误"
        }
        return msg

    }

    @RequestMapping(value = ["/article", "/article/"], method = [RequestMethod.GET])
    fun getArticles(pagination: Pagination): Message {
        val msg = Message()
        try {
            val articles = articleMapper.select(pagination)
            val count = articleMapper.count()
            msg.map("articles", articles)
            msg.map("count", count)
            msg.map("pagination", pagination)
        } catch (e: Exception) {
            msg.code = 500
            msg.info = e.message ?: "未知错误"
        }
        return msg
    }

    @RequestMapping(value = ["/article", "/article/"], method = [RequestMethod.PUT])
    fun updateArticle(article: Article): Message {
        val msg = Message()
        if (article.id == -1) {
            msg.code = 400
            msg.info = "文章id不存在"
        } else if (articleMapper.selectByPrimaryKey(article.id) == null) {
            msg.code = 400
            msg.info = "文章不存在"
        } else {
            try {
                if (articleMapper.update(article) == 1) {
                    msg.code = 200
                    msg.info = "更新文章成功"
                    msg.map("article", articleMapper.selectByPrimaryKey(article.id) ?: "")
                } else {
                    msg.code = 500
                    msg.info = "更新文章错误"
                }
            } catch (e: Exception) {
                msg.code = 500
                msg.info = e.message ?: ""
            }
        }
        return msg
    }

    @RequestMapping(value = ["/articles", "/articles/"], method = [RequestMethod.GET])
    fun getArticleByExample(article: Article, pagination: Pagination): Message {
        val msg = Message()
        try {
            val articles = articleMapper.selectByExample(article, pagination)
            val count = articleMapper.countByExample(article)
            msg.info = "查询成功"
            msg.map("articles", articles)
            msg.map("count", count)
            msg.map("selection", getSelection(article))
            msg.map("pagination", pagination)
        } catch (e: Exception) {
            msg.code = 500
            msg.info = e.message ?: ""
        }
        return msg
    }

    @RequestMapping(value = ["/article/count", "/article/count/"], method = [RequestMethod.GET])
    fun getCount(): Message {
        val msg = Message()
        try {
            val count = articleMapper.count()
            msg.info = "查询成功"
            msg.map("count", count)
        } catch (e: Exception) {
            msg.code = 500
            msg.info = e.message ?: ""
        }
        return msg
    }

    @RequestMapping(value = ["/article/counts", "/article/counts/"], method = [RequestMethod.GET])
    fun getCountByExample(article: Article): Message {
        val msg = Message()
        try {
            val count = articleMapper.countByExample(article)
            msg.info = "查询成功"
            msg.map("count", count)
            msg.map("selection", article)
        } catch (e: Exception) {
            msg.code = 500
            msg.info = e.message ?: ""
        }
        return msg
    }

    private fun getSelection(article: Article): Map<String, String> {
        val map: MutableMap<String, String> = mutableMapOf()
        if (article.author.account.isNotBlank()) {

            map["author"] = article.author.account
        }
        if (article.title.isNotBlank()) {
            map["title"] = article.title
        }
        if (article.catalog.id != -1) {
            map["catalog"] = article.catalog.id.toString()
        }
        if (article.tag.isNotBlank()) {
            map["tag"] = article.tag
        }
        if (article.datetime.isNotBlank()) {
            map["datetime"] = article.datetime
        }
        return map
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