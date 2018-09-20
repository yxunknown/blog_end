package com.hercats.dev.commonbase.tool

import java.math.BigInteger
import java.nio.charset.Charset
import java.security.MessageDigest

fun sha(source: String): String {
    val inputData = source.toByteArray(charset = Charset.forName("UTF-8"))
    val msgDigest = MessageDigest.getInstance("SHA")
    val sha = BigInteger(msgDigest.digest(inputData)).abs()
    return sha.toString(32)
}

fun main(args: Array<String>) {
    println(sha("Loveyx.123"))
}