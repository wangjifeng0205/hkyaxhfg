package org.hkyaxhfg.commons.json

import org.hkyaxhfg.base.DateTime
import java.lang.reflect.Type
import java.text.ParseException
import java.util.*

/**
 * @author: wjf
 * @date: 2021/12/23 13:49
 *
 * 一系列的TypeAdapter, 用于json序列化和反序列化.
 */
// Date
class DateTypeAdapter : JsonSerializer<Date>, JsonDeserializer<Date?> {

    override fun serialize(src: Date?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return if (src == null) { JsonNull.INSTANCE } else { JsonPrimitive(DateTime.format(DateTime.datetime(src), DateTime.YYYY_MM_DD_hh_mm_ss)) }
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Date? {
        return json?.let {
            if (json.isJsonPrimitive) {
                return DateTime.matchParse(json.asString)
            }
            throw ParseException("匹配解析日期出现错误, 找不到一个符合 [${json.asString}] 的日期格式化", 0)
        }
    }

}