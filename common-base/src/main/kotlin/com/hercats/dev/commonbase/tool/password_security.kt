package com.hercats.dev.commonbase.tool

import java.math.BigInteger
import java.nio.charset.Charset
import java.security.MessageDigest
import java.util.*

fun sha(source: String): String {
    val inputData = source.toByteArray(charset = Charset.forName("UTF-8"))
    val msgDigest = MessageDigest.getInstance("SHA")
    val sha = BigInteger(msgDigest.digest(inputData)).abs()
    return sha.toString(32)
}

fun base64(source: String): String {
    return Base64.getEncoder().encodeToString(source.toByteArray(charset = Charsets.UTF_8))
}

fun deBase64(source: String): String {
    return Base64.getDecoder().decode(source).toString(Charsets.UTF_8)
}

fun main(args: Array<String>) {
    println(sha("12345678"))

}