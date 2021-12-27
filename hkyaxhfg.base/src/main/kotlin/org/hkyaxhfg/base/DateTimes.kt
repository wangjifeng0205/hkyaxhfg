package org.hkyaxhfg.base

import org.hkyaxhfg.base.DateTime.Companion.format

/**
 * @author: wjf
 * @date: 2021/12/23 15:02
 *
 * 日期时间相关操作.
 */

/**
 * @see [format] 格式化日期时间.
 *
 * @param [pattern] 日期格式化
 *
 * @return [String] 格式化之后的日期字符串
 */
fun DateTime.format(pattern: String): String {
    return format(this, pattern)
}

/**
 * @see [formatOrNull] 格式化日期时间.
 *
 * @param [pattern] 日期格式化
 *
 * @return [String] 格式化之后的日期字符串, this 为 null 时, 返回 null.
 */
fun DateTime?.formatOrNull(pattern: String): String? {
    return this?.let {
        format(pattern)
    }
}