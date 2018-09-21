package com.hercats.dev.commonbase.redis

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class Redis(@Autowired val redisTemplate: RedisTemplate<String, String>) {

    /**
     * add or update a [key] with [value] and [expire]
     */
    fun set(key: String, value: String, expire: Long = 60 * 60 * 6): Boolean = try {
        redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }

    /**
     * get value of key
     */
    infix fun get(key: String): String {
        return redisTemplate.opsForValue().get(key) ?: ""
    }

    /**
     * get expiration of key
     */
    infix fun ttl(key: String) = redisTemplate.getExpire(key)

    /**
     * delete value by key
     */
    infix fun del(key: String): Boolean = try {
        redisTemplate.delete(key)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }

    /**
     * check if the redis contains the key
     */
    fun exists(key: String): Boolean = try {
        redisTemplate.hasKey(key)
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}