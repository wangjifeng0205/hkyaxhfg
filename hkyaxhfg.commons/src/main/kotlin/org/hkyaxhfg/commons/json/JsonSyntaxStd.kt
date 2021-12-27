package org.hkyaxhfg.commons.json

/**
 * @author: wjf
 * @date: 2021/12/21 14:52
 *
 * @see [JsonSyntaxStd] 一些自定义的json句法操作.
 */
interface JsonSyntaxStd {

    /**
     * @see [analyze] 解析token.
     *
     * @return [Json] 解析json-token之后返回的 [Json]
     */
    fun analyze(): Json

    /**
     * @see [hasToken] 验证key是否包含token.
     *
     * @param [key] key
     *
     * @return [Boolean] 验证是否包含此json-token
     */
    fun hasToken(key: String): Boolean

    /**
     * @see [getNewKeys] 获取解析完json-token之后的新key, 新key列表都是排除了当前json-token之后的key列表.
     *
     * @return [List] [JsonKey] 新key列表
     */
    fun getNewKeys(): List<JsonKey>

    /**
     * @see [ThisToken] [#this]句法规则: [#this]代表当前json对象.
     *
     * @param [json] 被解析的 [Json] 对象, 每一个被解析的 [Json] 对象都应该包含 [#this] json-token
     * @param [key] 被解析的json-key
     *
     * @throws [JsonSyntaxException] 当 [json] 不包含 [#this] 时将抛出
     */
    class ThisToken(private val json: Json, private val key: String) : JsonSyntaxStd {

        /**
         * @see [jsonKeys] 解析完之后的新key.
         */
        private var jsonKeys: List<JsonKey>? = null

        override fun analyze(): Json {
            if (!hasToken(key)) {
                throw JsonSyntaxException("json-key: [$key] 必须以 [#this] 开始")
            }

            val jsonKeys = JsonKey.analyze(key)
            val thisToken = jsonKeys[0]

            if (thisToken.jsonType != json.jsonType) {
                throw JsonSyntaxException("json-analyze 错误, json.jsonType: [${json.jsonType?.name}], thisToken.jsonType: [${thisToken.jsonType}]")
            }

            this.jsonKeys = jsonKeys.subList(1, jsonKeys.size)

            when (json.jsonType) {
                JsonType.OBJECT -> {
                    if (json.jsonElement !is JsonObject) {
                        throw JsonSyntaxException("json-value 必须是 [JsonObject], 当前是: [${json.jsonElement!!::class.qualifiedName}]")
                    }
                    // this
                    return json
                }
                JsonType.ARRAY -> {
                    if (json.jsonElement !is JsonArray) {
                        throw JsonSyntaxException("json-value 必须是 [JsonArray], 当前是: [${json.jsonElement!!::class.qualifiedName}]")
                    }
                    // this[i]
                    return Json((json.jsonElement as JsonArray).get(thisToken.index))
                }
                JsonType.PRIMITIVE -> {
                    if (json.jsonElement !is JsonPrimitive) {
                        throw JsonSyntaxException("json-value 必须是 [JsonPrimitive], 当前是: [${json.jsonElement!!::class.qualifiedName}]")
                    }
                    // this
                    return json
                }
                JsonType.NULL -> {
                    if (json.jsonElement !is JsonNull) {
                        throw JsonSyntaxException("json-value 必须是 [JsonNull], 当前是: [${json.jsonElement!!::class.qualifiedName}]")
                    }
                    // this
                    return json
                }
                else -> throw JsonSyntaxException("不支持的 jsonType: [${json.jsonType}]")
            }
        }

        override fun hasToken(key: String): Boolean {
            return key.trimStart().startsWith(THIS_TOKEN)
        }

        override fun getNewKeys(): List<JsonKey> {
            return this.jsonKeys ?: listOf()
        }

        companion object {

            /**
             * @see [THIS_TOKEN] #this.
             */
            private const val THIS_TOKEN = "#this"

        }

    }

    /**
     * @see [JsonKey] 用来承载 [get] 函数的key解析之后的结果.
     *
     * @param [key] 解析之后的json-key
     * @param [jsonType] 当前json-key对应的json数据类型
     * @param [index] 当为数组时才有效
     */
    data class JsonKey (
        val key: String,
        val jsonType: JsonType,
        val index: Int = -1
    ) {
        companion object {

            /**
             * @see [arrayPrefix] 数组前缀.
             */
            private const val arrayPrefix = "["

            /**
             * @see [arraySuffix] 数组后缀.
             */
            private const val arraySuffix = "]"

            /**
             * @see [analyze] 解析key.
             *
             * @param [key] 需要解析的key
             *
             * @return [List] [JsonKey] 被解析之后的json-key
             */
            fun analyze(key: String): List<JsonKey> {
                if (key.isBlank()) {
                    return listOf()
                }
                val keys = key.splitToSequence(".")

                val jsonKeys = mutableListOf<JsonKey>()
                keys.asSequence().forEach {
                    if (isJsonArray(it)) {
                        jsonKeys.add(assembly(it))
                    } else {
                        jsonKeys.add(JsonKey(it, JsonType.OBJECT))
                    }
                }
                return jsonKeys
            }

            /**
             * @see [isJsonArray] 是否是json数组.
             *
             * @param [key] key
             *
             * @return [Boolean] 是否是数组
             */
            private fun isJsonArray(key: String): Boolean {
                return key.trimEnd().endsWith(arraySuffix)
            }

            /**
             * @see [assembly] 组装 [JsonKey].
             *
             * @param [key] key
             *
             * @return [JsonKey] 一个解析完成的 [JsonKey]
             */
            private fun assembly(key: String): JsonKey {
                val trimKey = key.trimStart().trimEnd()
                val prefixIndex = trimKey.indexOf(arrayPrefix)
                val suffixIndex = trimKey.indexOf(arraySuffix)
                val index = trimKey.substring(prefixIndex + 1, suffixIndex).toInt()
                val keyName = trimKey.substring(0, prefixIndex)
                return JsonKey(keyName, JsonType.ARRAY, index)
            }

        }
    }

}