package com.hercats.dev.albumprovider.controller

import com.hercats.dev.commonbase.mapper.AlbumMapper
import com.hercats.dev.commonbase.model.Album
import com.hercats.dev.commonbase.model.Message
import com.hercats.dev.commonbase.model.Pagination
import com.hercats.dev.commonbase.model.SqlDate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController


@RestController
class AlbumController(@Autowired val albumMapper: AlbumMapper) {

    @RequestMapping(value = ["/hello", "/hello/"], method = [RequestMethod.GET])
    fun hello(): Message {
        val message = Message()
        message.info = "hello"
        return message
    }

    @RequestMapping(value = ["/album", "/album/"], method = [RequestMethod.POST])
    fun addAlbum(album: Album): Message {
        val msg = Message()
        try {
            album.createDate = SqlDate().toString()
            if (albumMapper.insert(album) == 1) {
                msg.info = "添加成功"
                msg.map("album", album)
            } else {
                msg.code = 500
                msg.info = "添加失败"
            }
        } catch (e: Exception) {
            msg.code = 500
            msg.info = e.message ?: ""
        }
        return msg
    }

    @RequestMapping(value = ["/album", "/album/"], method = [RequestMethod.PUT])
    fun updateAlbum(album: Album): Message {
        val msg = Message()
        when {
            (album.id == -1) -> {
                msg.code = 400
                msg.info = "缺少id参数"
            }
            (albumMapper.selectByPrimaryKey(album.id) == null) -> {
                msg.code = 400
                msg.info = "相册不存在"
            }
            else -> {
                try {
                    if (albumMapper.update(album) == 1) {
                        msg.code = 200
                        msg.info = "更新相册信息成功"
                        msg.map("album", albumMapper.selectByPrimaryKey(album.id) ?: "")
                    } else {
                        msg.code = 500
                        msg.info = "更新相册信息失败"
                    }
                } catch (e: Exception) {
                    msg.code = 500
                    msg.info = e.message ?: "未知错误"
                }
            }
        }
        return msg
    }

    @RequestMapping(value = ["/album/{id}", "/album/{id}/"], method = [RequestMethod.GET])
    fun getAlbumById(@PathVariable("id") id: Int): Message {
        val msg = Message()
        try {
            val album = albumMapper.selectByPrimaryKey(id)
            if (album == null) {
                msg.code = 400
                msg.info = "相册信息不存在"
            } else {
                msg.code = 200
                msg.info = "查询相册信息成功"
                msg.map("album", album)
            }
        } catch (e: Exception) {
            msg.code = 500
            msg.info = e.message ?: "未知错误"
        }
        return msg
    }

    @RequestMapping(value = ["/album", "/album/"], method = [RequestMethod.GET])
    fun getAlbums(pagination: Pagination): Message {
        val msg = Message()
        try {
            val albums = albumMapper.select(pagination)
            msg.info = "查询成功"
            msg.map("count", albumMapper.count())
            msg.map("pagination", pagination)
            msg.map("albums", albums)
        } catch (e: Exception) {
            msg.code = 500
            msg.info = e.message ?: "未知错误"
        }
        return msg
    }

}