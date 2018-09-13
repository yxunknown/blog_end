package com.hercats.dev.commonbase.model

import org.omg.CORBA.Object
import java.util.*

data class Album(
        var id: Int,
        var title: String?,
        var description: String?,
        var cover: Photo?,
        var createDate: Date)

data class Article(
        var id: Int,
        var author: User,
        var title: String,
        var content: String,
        var datetime: Date,
        var cover: Photo?,
        var tag: String?,
        var catalog: ArticleCatalog)

data class ArticleCatalog(
        var id: Int,
        var catalog: String)

data class Card(
        var id: Int,
        var author: User,
        var content: String,
        var background: Photo?,
        var bgColor: String?,
        var textColor: String?)

data class Photo(
        var id: Int,
        var path: String,
        var description: String?,
        var latitude: Double?,
        var longitude: Double?,
        var md5: String,
        var uploadDate: Date?)

data class User(
        var account: String = "",
        var password: String = "",
        var nickname: String? = "",
        var brief: String? = "",
        var status: UserStatus = UserStatus(1, "正常"))



data class UserStatus(
        var id: Int = 1,
        var description: String = "未激活")

//default pagination parameter
data class Pagination(
        val start: Long = 0,
        val limit: Long = 20)

class Message{
    var code: Int = 200
    var info: String = ""
    val map: MutableMap<String, Any> = mutableMapOf()

    fun map(key: String, value: Any) {
        this.map[key] = value
    }
}