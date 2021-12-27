package org.hkyaxhfg.base

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author: wjf
 * @date: 2021/12/23 13:53
 *
 * @see [DateTime] 日期时间类, 处理日期时间, 继承了 [java.util.Date].
 */
class DateTime() : Date() {

    /**
     * @see [date] 通过一个 [Date] 构建一个 [DateTime].
     */
    constructor(date: Date) : this() {
        this.time = date.time
    }

    companion object {

        /**
         * @see [YYYY_MM_DD_hh_mm_ss] 日期格式化.
         */
        const val YYYY_MM_DD_hh_mm_ss = "yyyy-MM-dd HH:mm:ss"

        /**
         * @see [YYYY_MM_DD] 日期格式化.
         */
        const val YYYY_MM_DD = "yyyy-MM-dd"

        /**
         * @see [hh_mm_ss] 日期格式化.
         */
        const val hh_mm_ss = "HH:mm:ss"

        /**
         * @see [PATTERNS] 存储日期格式化.
         */
        val PATTERNS = mutableListOf<String>()

        init {
            PATTERNS.add(YYYY_MM_DD_hh_mm_ss)
            PATTERNS.add(YYYY_MM_DD)
            PATTERNS.add(hh_mm_ss)
        }

        /**
         * @see [now] 获取当前时间.
         *
         * @return [DateTime] 日期时间
         */
        fun now(): DateTime {
            return DateTime()
        }

        /**
         * @see [datetime] 获取日期时间类, 主要用如 [Date] 和 [DateTime] 之间的转换.
         *
         * @param [date] 日期, 默认为当前时间
         *
         * @return [DateTime] 日期时间
         */
        fun datetime(date: Date = now()): DateTime {
            return DateTime(date)
        }

        /**
         * @see [format] 格式化日期时间.
         *
         * @param [dateTime] 日期时间
         * @param [pattern] 日期格式化
         *
         * @return [String] 格式化之后的日期字符串
         */
        fun format(dateTime: DateTime, pattern: String): String {
            return formatter(pattern).format(dateTime)
        }

        /**
         * @see [parse] 解析日期字符串为日期时间.
         *
         * @param [dateTime] 日期字符串
         * @param [pattern] 日期格式化
         *
         * @return [DateTime] 日期时间
         */
        fun parse(dateTime: String, pattern: String): DateTime {
            return DateTime(formatter(pattern).parse(dateTime))
        }

        /**
         * @see [formatter] 获取一个格式化器.
         *
         * @param [pattern] 日期格式化
         *
         * @return [SimpleDateFormat]
         */
        fun formatter(pattern: String = YYYY_MM_DD_hh_mm_ss): SimpleDateFormat {
            return SimpleDateFormat(pattern)
        }

        /**
         * @see [matchParse] 匹配解析日期字符串.
         *
         * @param [dateTime] 日期字符串
         *
         * @return [DateTime] 日期时间
         */
        fun matchParse(dateTime: String, index: Int = 0): DateTime {
            return try {
                parse(dateTime, PATTERNS[index])
            } catch (e: ParseException) {
                if (index < PATTERNS.size - 1) {
                    matchParse(dateTime, index + 1)
                } else {
                    throw ParseException("匹配解析日期出现错误, 找不到一个符合 [${dateTime}] 的日期格式化", 0)
                }
            }
        }
    }

}