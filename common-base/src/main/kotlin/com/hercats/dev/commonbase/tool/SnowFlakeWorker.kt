package com.hercats.dev.commonbase.tool
/**
* 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000 <br>
* 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0<br>
* 41位时间截(毫秒级)，注意，41位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)
* 得到的值），这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下下面程序IdWorker类的startTime属性）。
* 41位的时间截，可以使用69年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69<br>
* 10位的数据机器位，可以部署在1024个节点，包括5位datacenterId和5位workerId<br>
* 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号<br>
* 加起来刚好64位，为一个Long型。<br>
* SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高，
**/
class SnowFlakeWorker {
    private val twepoch = 1420041600000L
    private val workerIdBits = 5L
    private val dataCenterIdBits = 5L
    private val maxWorkerId = -1L xor (-1L shl workerIdBits.toInt())
    private val maxDataCenterId = -1L xor (-1L shl dataCenterIdBits.toInt())
    private val sequenceBits = 12L
    private val workerIdShift = sequenceBits
    private val dataCenterIdShift = sequenceBits + workerIdBits
    private val timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits
    private val sequenceMask = -1L xor (-1L shl sequenceBits.toInt())
    private var sequence = 0L
    private var lastTimestamp = -1L
    private var dataCenterId: Long = 0
    private var workerId: Long = 0


    constructor(workerId: Long,
                dataCenterId: Long) {
        if (workerId !in 0..maxWorkerId) {
            throw IllegalArgumentException("worker id can't be greater than $maxWorkerId or less than 0")
        }
        if (dataCenterId !in 0..maxDataCenterId) {
            throw IllegalArgumentException("data center id can't be greater than $maxDataCenterId or less than 0")
        }
        this.dataCenterId = dataCenterId
        this.workerId = workerId
    }

    @Synchronized fun nextId(): Long {

        var timestamp = timestamp()
        if (timestamp < lastTimestamp) {
            throw RuntimeException("Clock move backwards, Refusing to generate id for ${lastTimestamp - timestamp} milliseconds")
        }
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) and sequenceMask
            if (sequence == 0L) {
                timestamp = tillNextMillis(lastTimestamp)
            }
        } else {
            sequence = 0L
        }
        lastTimestamp = timestamp
        return ((timestamp - twepoch) shl timestampLeftShift.toInt())
                .or(dataCenterId shl dataCenterIdShift.toInt())
                .or(workerId shl workerIdShift.toInt())
                .or(sequence)
    }

    private fun tillNextMillis(lastTimestamp: Long): Long {
        var timestamp = timestamp()
        while (timestamp <= lastTimestamp) {
            timestamp = timestamp()
        }
        return timestamp
    }

    private fun timestamp(): Long = System.currentTimeMillis()

}