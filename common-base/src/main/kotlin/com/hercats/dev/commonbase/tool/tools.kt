package com.hercats.dev.commonbase.tool

import kotlin.math.absoluteValue

fun getId(): Long = SnowFlakeWorker(1, 2).nextId().absoluteValue