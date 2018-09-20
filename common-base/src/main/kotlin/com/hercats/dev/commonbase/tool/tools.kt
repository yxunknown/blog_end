package com.hercats.dev.commonbase.tool

import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.absoluteValue

fun getId(): Long = SnowFlakeWorker(1, 2).nextId().absoluteValue

fun md5(bytes: ByteArray): String {
    return try {
        val digest = MessageDigest.getInstance("MD5")
        val out = BigInteger(1, digest.digest(bytes))
        out.toString(16)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

//username with random number to generate token by md5 method
fun token(username: String) = md5("$username${getId()}".toByteArray())
