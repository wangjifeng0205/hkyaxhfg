package org.hkyaxhfg.commons.json

import com.google.gson.LongSerializationPolicy
import org.hkyaxhfg.base.reflect.Reflector
import org.hkyaxhfg.base.reflect.new
import org.hkyaxhfg.commons.knum.Knum
import org.hkyaxhfg.commons.knum.KnumTypeAdapter
import java.util.*

/**
 * @author: wjf
 * @date: 2021/12/20 17:22
 *
 * @see [JsonHandler] json数据格式处理人, 用于操作json数据.
 */
object JsonHandler {

    /**
     * @see [jsonProcessor] 内置的json序列化和反序列化处理器.
     */
    val jsonProcessor: JsonCore = JsonBuilder()
        .serializeNulls()
        .setLongSerializationPolicy(LongSerializationPolicy.STRING)
        .registerTypeAdapter(Knum::class.java, KnumTypeAdapter())
        .registerTypeAdapter(Date::class.java, DateTypeAdapter())
        .create()


    /**
     * @see [fromObj] 将任意一个对象序列化为json字符串.
     *
     * @param [any] 任意一个不为null的对象
     *
     * @return [String] json字符串
     */
    fun fromObj(any: Any?): String = jsonProcessor.toJson(any)

    /**
     * @see [toObj] 将json字符串反序列化成为一个对象 [T].
     *
     * @param [json] json字符串
     * @param [T] 要反序列化的类型
     *
     * @return [T] 解析返回的对象类型
     */
    inline fun <reified T> toObj(json: String): T = jsonProcessor.fromJson(json, T::class.java)

    /**
     * @see [fromJson] 将任意一个对象序列化为json字符串.
     *
     * @param [json] 一个 [Json] 对象
     *
     * @return [String] json字符串
     */
    fun fromJson(json: Json?): String {
        if (json == null) {
            return JsonNull.INSTANCE.toString()
        }
        return json.jsonElement.toString()
    }

    /**
     * @see [jsonToObj] 将json字符串反序列化成为一个对象 [T].
     *
     * @param [json] 一个 [Json] 对象
     * @param [key] key
     * @param [T] 要反序列化的类型
     *
     * @return [T] 解析返回的对象类型
     */
    inline fun <reified T> jsonToObj(json: Json?, key: String = ""): T? {
        if (json == null || key.isBlank()) {
            return Reflector.new()
        }
        return json.getAsT<T>(key)
    }

    /**
     * @see [toJson] 将任意对象转换为 [Json].
     *
     * @param [any] 任意对象,
     *
     * @return [Json] json统一对象
     */
    fun toJson(any: Any?): Json {
        if (any == null) {
            return Json(JsonNull.INSTANCE)
        }
        if (any is Json) {
            return any
        }
        return when (any) {
            is JsonElement -> Json(any)
            else -> Json(toJsonElement(any))
        }
    }

    /**
     * @see [toJsonElement] 将任意对象转换为 [JsonElement].
     *
     * @param [any] 任意对象
     *
     * @return [JsonElement] JsonElement
     */
    fun toJsonElement(any: Any?): JsonElement {
        if (any == null) {
            return JsonNull.INSTANCE
        }
        return jsonProcessor.toJsonTree(any)
    }

}