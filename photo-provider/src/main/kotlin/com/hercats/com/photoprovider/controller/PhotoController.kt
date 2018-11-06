package com.hercats.com.photoprovider.controller

import com.hercats.dev.commonbase.mapper.AlbumMapper
import com.hercats.dev.commonbase.mapper.PhotoMapper
import com.hercats.dev.commonbase.model.Message
import com.hercats.dev.commonbase.model.Pagination
import com.hercats.dev.commonbase.model.Photo
import com.hercats.dev.commonbase.model.SqlDate
import com.hercats.dev.commonbase.tool.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.FileSystemResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.util.*

@RestController
class PhotoController(@Autowired val photoMapper: PhotoMapper,
                      @Autowired val albumMapper: AlbumMapper) {

    val path = "/Users/hercat/Documents/photo_test"

    @RequestMapping(value = ["/hello", "/hello/"], method = [RequestMethod.GET])
    fun hello(): Message {
        val msg = Message()
        msg.info = "hello"
        return msg
    }

    @RequestMapping(value = ["/photo", "/photo/"], method = [RequestMethod.POST])
    fun uploadPhoto(photo: Photo): Message {
        val msg = Message()
        if (photo.path.isBlank()) {
            msg error_400 "请提供照片url"
        } else {
            if (photoMapper.insert(photo) == 1) {
                msg ok "上传照片成功"
                msg.map("photo", photo)
            } else {
                msg error_500 "照片存储错误"
            }
        }
        return msg
    }

    @RequestMapping(value = ["/photo", "/photo/"], method = [RequestMethod.PUT])
    fun updatePhotoInfo(photo: Photo): Message {
        val msg = Message()
        try {
            if (photo.id == -1) {
                msg.code = 400
                msg.info = "未提交照片id参数"
            } else if (photoMapper.selectByPrimaryKey(photo.id) == null) {
                msg.code = 400
                msg.info = "照片不存在"
            } else {
                if (photoMapper.update(photo) == 1) {
                    msg.code = 200
                    msg.info = "更新照片成功"
                    msg.map("photo", photoMapper.selectByPrimaryKey(photo.id)?.apply { path = "/photo/download/$id" }
                            ?: "")
                } else {
                    msg.code = 500
                    msg.info = "更新失败"
                }
            }
        } catch (e: Exception) {
            msg.code = 500
            msg.info = e.message ?: "未知错误"
        }
        return msg
    }


    @RequestMapping(value = ["/photo/download/{id}", "/photo/download/{id}"], method = [RequestMethod.GET])
    fun downloadPhoto(@PathVariable("id") id: Int): ResponseEntity<FileSystemResource> {
        val photo = photoMapper.selectByPrimaryKey(id)
        return if (photo == null) {
            ResponseEntity.badRequest()
                    .body(null)
        } else {
            val file = File(photo.path)
            val headers = HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate")
            headers.add("Content-Disposition", "attachment; filename=${file.name}")
            headers.add("Pragma", "no-cache")
            headers.add("Expires", "0")
            headers.add("Last-Modified", Date().toString())
            headers.add("ETag", System.currentTimeMillis().toString())
            ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(FileSystemResource(file))
        }
    }

    @RequestMapping(value = ["/photo/{photoId}/{albumId}", "/photo/{photoId}/{albumId}/"], method = [RequestMethod.POST])
    fun addPhotoToAlbum(@PathVariable("photoId") photoId: Int,
                        @PathVariable("albumId") albumId: Int): Message {
        val msg = Message()
        when {
            (photoMapper.selectByPrimaryKey(photoId) == null) -> {
                msg.code = 400
                msg.info = "照片不存在"
            }
            (albumMapper.selectByPrimaryKey(albumId) == null) -> {
                msg.code = 400
                msg.info = "相册不存在"
            }
            else -> {
                try {
                    if (photoMapper.addPhotoIntoAlbum(albumId, photoId) == 1) {
                        msg.code = 200
                        msg.info = "添加照片成功"
                    } else {
                        msg.code = 500
                        msg.info = "添加照片失败"
                    }
                } catch (e: Exception) {
                    msg.code = 500
                    msg.info = e.message ?: "未知错误"
                }
            }
        }
        return msg
    }

    @RequestMapping(value = ["/photo/album/{albumId}", "/photo/album/{albumId}"], method = [RequestMethod.GET])
    fun getAlbumPhotos(@PathVariable("albumId") albumId: Int,
                       pagination: Pagination): Message {
        val msg = Message()
        try {
            val album = albumMapper.selectByPrimaryKey(albumId)
            if (album == null) {
                msg.code = 400
                msg.info = "相册不存在"
            } else {
                val photos = photoMapper.getAlbumPhotos(pagination, albumId)
                msg.code = 200
                msg.info = "查询成功"
                photos.apply {
                    this.forEach { photo -> photo.path = "http://127.0.0.1:8083/photo/download/${photo.id}" }
                }
                msg.map("album", album)
                msg.map("count", photoMapper.countAlbumPhotos(albumId))
                msg.map("pagination", pagination)
                msg.map("photos", photos)
            }
        } catch (e: Exception) {
            msg.code = 500
            msg.info = e.message ?: "未知错误"
        }
        return msg
    }

    fun saveToFile(file: MultipartFile): String {
        val dir = File(path)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return try {
            val f = File("${dir.path}/${getId()}.${file.originalFilename?.substringAfterLast(".")}")
            val writer = FileOutputStream(f)
            writer.write(file.bytes)
            writer.close()
            f.toString()
        } catch (e: Exception) {
            ""
        }
    }
}