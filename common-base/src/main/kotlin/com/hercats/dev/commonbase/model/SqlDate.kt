package com.hercats.dev.commonbase.model

import java.text.SimpleDateFormat
import java.util.*

class SqlDate: Date() {

    override fun toString(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
        return dateFormat.format(this)
    }
}