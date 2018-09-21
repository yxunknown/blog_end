package com.hercats.dev.commonbase.model

data class Album(
        var id: Int = -1,
        var title: String = "",
        var description: String = "",
        var cover: Photo = Photo(),
        var createDate: String = "")

data class Article(
        var id: Int = -1,
        var author: User = User(),
        var title: String = "",
        var content: String = "",
        var datetime: String = "",
        var cover: Photo = Photo(),
        var tag: String = "",
        var catalog: ArticleCatalog = ArticleCatalog())

data class ArticleCatalog(
        var id: Int = -1,
        var catalog: String = "其他")

data class Card(
        var id: Int = -1,
        var author: User = User(),
        var content: String = "",
        var background: Photo = Photo(),
        var bgColor: String = "#de1c31",
        var textColor: String = "#ffffff")

data class Photo(
        var id: Int = -1,
        var path: String = "",
        var description: String = "",
        var latitude: Double = -91.0,
        var longitude: Double = -181.0,
        var md5: String = "",
        var uploadDate: String = "")

data class User(
        var account: String = "",
        var password: String = "",
        var nickname: String = "",
        var brief: String = "",
        var status: UserStatus = UserStatus(-1, ""))



data class UserStatus(
        var id: Int = -1,
        var description: String = "")

//default pagination parameter
data class Pagination(
        val start: Long = 0,
        val limit: Long = 20)

data class Token(
        val username: String,
        val token: String,
        val tokenExpire: Long = 60 * 60 * 24,
        val refreshToken: String,
        val refreshTokenExpire: Long = tokenExpire * 7)

class Message{
    var code: Int = 200
    var info: String = ""
    val map: MutableMap<String, Any> = mutableMapOf()

    fun map(key: String, value: Any) {
        this.map[key] = value
    }
}