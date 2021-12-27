package org.hkyaxhfg.commons.json

/**
 * @author: wjf
 * @date: 2021/12/21 15:34
 *
 * @see [JsonType] root节点json节点类型.
 */
enum class JsonType {

    /**
     * @see [OBJECT] OBJECT, json对象数据类型, 了解更多[com.google.gson.JsonObject].
     */
    OBJECT,

    /**
     * @see [ARRAY] ARRAY, json数组数据类型, 了解更多[com.google.gson.JsonArray].
     */
    ARRAY,

    /**
     * @see [NULL] NULL, json-null数据类型, 了解更多[com.google.gson.JsonNull].
     */
    NULL,

    /**
     * @see [PRIMITIVE] PRIMITIVE, json原始数据类型, 了解更多[com.google.gson.JsonPrimitive].
     */
    PRIMITIVE

}