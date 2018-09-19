/**
 * the mapper interfaces definition
 * @author hercats
 * @date 2018-09-12 16:58:54
 */

package com.hercats.dev.commonbase.mapper

import com.hercats.dev.commonbase.model.*
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param

/**
 * album mapper interface
 */
@Mapper
interface AlbumMapper {

    /**
     * insert an new album into database
     * @param album the new album
     * @return 1 for insert success
     *         otherwise for failed
     */
    fun insert(@Param("album") album: Album): Int

    /**
     * count all albums from database
     * @return the amount of albums in the database
     */
    fun count(): Long

    /**
     * count the amount of albums by selected conditions
     * @param album the selected conditions
     * @return the amount of albums that satisfy selected conditions
     */
    fun countByExample(@Param("album") album: Album): Long

    /**
     * select album from database unconditionally
     * @param pagination pagination parameter
     *        default value is start = 0 and limit = 20
     * @return a collection contains albums
     */
    fun select(@Param("pagination") pagination: Pagination = Pagination()): List<Album>

    /**
     * select an album by primary key
     * @param id the id of album
     * @return an album which id is equal to {id} or null
     */
    fun selectByPrimaryKey(@Param("id") id: Int): Album?

    /**
     * select albums with conditions
     * @param album the select conditions
     * @param pagination the pagination parameters,
     *        the default value is start = 0 and limit = 20
     * return a collection contains condition-satisfied album
     */
    fun selectByExample(@Param("album") album: Album,
                        @Param("pagination") pagination: Pagination = Pagination()): List<Album>

    /**
     * update an album with new album
     * @param album the new album
     * @return 1 if success or other if fail
     */
    fun update(@Param("album") album: Album): Int

    /**
     * delete an album by its primary key
     * @param id primary key of album
     * @return 1 for success and other for failed
     */
    fun deleteByPrimaryKey(@Param("id") id: Int): Long

    /** delete album by select condition
     * all album satisfy the condition will be deleted
     * @param album the select condition
     * @return the amount of deleted album
     *
     */
    fun deleteByExample(@Param("album") album: Album): Long
}

/**
 * article mapper interface
 */
@Mapper
interface ArticleMapper {

    /**
     * insert a new article into database
     * @param article the new article
     * @return 1 for success other for failed
     */
    fun insert(@Param("article") article: Article): Int

    fun count(): Long

    fun countByExample(@Param("article") article: Article): Long

    fun select(@Param("pagination") pagination: Pagination = Pagination()): List<Article>

    fun selectByPrimaryKey(@Param("id") id: Int): Article?

    fun selectByExample(@Param("article") article: Article,
                        @Param("pagination") pagination: Pagination = Pagination()): List<Article>

    fun update(@Param("article") article: Article): Int

    fun deleteByPrimaryKey(@Param("id") id: Int): Long

    fun deleteByExample(@Param("article") article: Article): Long
}

@Mapper
interface CardMapper {
    fun insert(@Param("card") card: Card): Int
    fun count(): Long
    fun countByExample(@Param("card") card: Card): Long
    fun select(@Param("pagination") pagination: Pagination = Pagination()): List<Card>

    fun selectByPrimaryKey(@Param("id") id: Int): Card

    fun selectByExample(@Param("card") card: Card,
                        @Param("pagination") pagination: Pagination = Pagination()): List<Card>

    fun update(@Param("card") card: Card): Int
    fun deleteByPrimaryKey(@Param("id") id: Int): Long
    fun deleteByExample(@Param("card") card: Card): Long
}

@Mapper
interface PhotoMapper {
    fun insert(@Param("photo") photo: Photo): Int
    fun count(): Long
    fun countByExample(@Param("photo") photo: Photo): Long
    fun select(@Param("pagination") pagination: Pagination = Pagination()): List<Photo>
    fun selectByPrimaryKey(@Param("id") id: Int): Photo?
    fun selectByExample(@Param("photo") photo: Photo,
                        @Param("pagination") pagination: Pagination = Pagination()): List<Photo>

    fun update(@Param("photo") photo: Photo): Int
    fun deleteByPrimaryKey(@Param("id") id: Int): Long
    fun deleteByExample(@Param("photo") photo: Photo): Long

    fun addPhotoIntoAlbum(@Param("albumId") albumId: Int,
                          @Param("photoId") photoId: Int): Int

    fun getAlbumPhotos(@Param("pagination") pagination: Pagination,
                       @Param("albumId") albumId: Int): List<Photo>

    fun countAlbumPhotos(@Param("albumId") albumId: Int): Long
}

@Mapper
interface UserMapper {
    fun insert(@Param("user") user: User): Int
    fun count(): Long
    fun countByExample(@Param("user") user: User): Long
    fun select(@Param("pagination") pagination: Pagination = Pagination()): List<User>
    fun selectByPrimaryKey(@Param("account") account: String): User?
    fun selectByExample(@Param("user") user: User,
                        @Param("pagination") pagination: Pagination = Pagination()): List<User>

    fun update(@Param("user") user: User): Int
    fun deleteByPrimaryKey(@Param("account") account: String): Long
    fun deleteByExample(@Param("user") user: User): Long
}