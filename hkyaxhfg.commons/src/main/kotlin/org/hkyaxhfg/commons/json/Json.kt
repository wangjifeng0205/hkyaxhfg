package org.hkyaxhfg.commons.json

import org.hkyaxhfg.base.reflect.Reflector
import org.hkyaxhfg.base.reflect.new

/**
 * @author: wjf
 * @date: 2021/12/20 20:54
 *
 * @see [Json] 统一化json的操作, 支持 [JsonSyntaxStd] 自定义句法操作.
 */
class Json() {

    /**
     * @see [jsonElement] 当此参数为空时, 内部元素则为 [JsonNull].
     */
    constructor(jsonElement: JsonElement?) : this() {
        this.jsonElement = jsonElement ?: JsonNull.INSTANCE
        when (jsonElement) {
            is JsonObject -> this.jsonType = JsonType.OBJECT
            is JsonArray -> this.jsonType = JsonType.ARRAY
            is JsonNull -> this.jsonType = JsonType.NULL
            is JsonPrimitive -> this.jsonType = JsonType.PRIMITIVE
        }
    }

    /**
     * @see [json] 此参数不可以为null.
     */
    constructor(json: Json) : this(json.jsonElement)

    /**
     * @see [jsonElement] 用来承载所有的json格式数据.
     */
    internal var jsonElement: JsonElement? = null

    /**
     * @see [jsonType] 标识root节点是什么json类型.
     */
    internal var jsonType: JsonType? = null

    /**
     * @see [addObject] 创建对象节点, 若最外层为空, 则会初始化, 可以是任意类型, 包括 [Json].
     *
     * @param [propertyName] json对象属性名称
     * @param [any] 任意对象
     */
    fun addObject(propertyName: String, any: Any?) {
        reset(JsonObject(), JsonType.OBJECT)
        val jsonObject = getNode<JsonObject>()

        if (any == null) {
            jsonObject.add(propertyName, JsonNull.INSTANCE)
            return
        }

        jsonObject.add(propertyName, parseToJsonElement(any))
    }

    /**
     * @see [addArray] 创建数组节点, 若最外层为空, 则会初始化, 可以是任意类型, 包括 [Json].
     *
     * @param [any] 任意对象, 会作为一个元素放入数组中
     */
    fun addArray(any: Any?) {
        reset(JsonArray(), JsonType.ARRAY)
        val jsonArray = getNode<JsonArray>()

        if (any == null) {
            jsonArray.add(JsonNull.INSTANCE)
            return
        }

        jsonArray.add(parseToJsonElement(any))
    }

    /**
     * @see [toJsonString] 返回json字符串.
     *
     * @return [String] 解析的json字符串
     */
    fun toJsonString(): String {
        if (jsonElement == null) {
            return JsonNull.INSTANCE.toString()
        }
        return jsonElement.toString()
    }

    /**
     * @see [getAsT] 获取数据, 并将其转换为对象.
     *
     * @param [key] key
     *
     * @return [T] 解析返回的对象类型
     */
    inline fun <reified T> getAsT(key: String): T {
        val any = get(key) ?: return Reflector.new()
        return if (any is JsonElement) {
            JsonHandler.jsonProcessor.fromJson(any, T::class.java)
        } else {
            JsonHandler.jsonProcessor.fromJson(JsonHandler.jsonProcessor.toJsonTree(any), T::class.java)
        }
    }

    /**
     * @see [get] 根据key获取对应的数据.
     *
     * @param [key] key
     * key的规则如下:
     * 对于对象: #this.propertyName
     * 对于数组: #this[0].propertyName
     * 对于混合: #this.propertyName[0].arrPropertyName
     *
     * @return [Any] 解析key返回的数据, 一般为 [JsonElement]
     */
    fun get(key: String): Any? {
        val thisToken: JsonSyntaxStd = JsonSyntaxStd.ThisToken(this, key)
        val json = thisToken.analyze()
        val jsonKeys = thisToken.getNewKeys()
        if (jsonKeys.isEmpty()) {
            return JsonNull.INSTANCE
        }
        @Suppress("UNCHECKED_CAST")
        return json.jsonElement?.let { getValue(parentNode = it, jsonKeys = jsonKeys) }
    }

    /**
     * @see [getValue] 递归拿取数据, 此处使用了尾递归, 提升性能.
     *
     * @param [parentNode] 父json节点
     * @param [jsonKeys] 解析之后的json-key
     * @param [index] 每次读取 [jsonKeys] 的索引位置
     *
     * @return [Any] 解析key返回的数据, 一般为 [JsonElement]
     */
    private tailrec fun getValue(
        parentNode: JsonElement = getNode(),
        jsonKeys: List<JsonSyntaxStd.JsonKey>,
        index: Int = 0
    ): Any? {
        val jsonKey = jsonKeys[index]
        val jsonNode: JsonElement = when (parentNode) {
            is JsonObject -> {
                val objectNode = getNode<JsonObject>(parentNode)
                when (jsonKey.jsonType) {
                    JsonType.OBJECT, JsonType.PRIMITIVE, JsonType.NULL -> objectNode.get(jsonKey.key)
                    JsonType.ARRAY -> getNode<JsonArray>(objectNode.get(jsonKey.key))[jsonKey.index]
                }
            }
            is JsonArray -> {
                val arrayNode = getNode<JsonArray>(parentNode)
                when (jsonKey.jsonType) {
                    JsonType.ARRAY -> arrayNode[jsonKey.index]
                    JsonType.OBJECT, JsonType.PRIMITIVE, JsonType.NULL -> throw JsonSyntaxException("不支持的 json-value: [${arrayNode}]")
                }
            }
            else -> throw JsonSyntaxException("不支持的 jsonNode: [${parentNode::class.qualifiedName}]")
        }
        if (index == jsonKeys.size - 1) {
            return jsonNode
        }
        return getValue(jsonNode, jsonKeys, index + 1)
    }

    /**
     * @see [reset] 初始化root节点json数据的初始化.
     *
     * @param [jsonElement] json节点, 可能是对象, 可能是数组
     * @param [jsonType] 标识最外层是对象还是数组
     */
    private fun reset(jsonElement: JsonElement, jsonType: JsonType) {
        if (this.jsonElement == null) {
            this.jsonElement = jsonElement
        }
        if (this.jsonType == null) {
            this.jsonType = jsonType
        }
    }

    /**
     * @see [] 获取root节点.
     *
     * @param [element] 一个 [JsonElement] 或者其子类, 默认为当前对象的 [JsonElement]
     *
     * @return [T] 要转换的类型
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T : JsonElement> getNode(element: JsonElement? = this.jsonElement): T {
        return element as T
    }

    /**
     * @see [parseToJsonElement] 解析对象 [any] , 组装并返回 [JsonElement].
     *
     * @param any 任意对象
     *
     * @return [JsonElement] 解析返回的对象类型
     */
    private fun parseToJsonElement(any: Any): JsonElement {
        return when (any) {
            is Json -> JsonHandler.jsonProcessor.toJsonTree(any.jsonElement)
            is JsonElement -> JsonHandler.jsonProcessor.toJsonTree(any)
            is String -> JsonPrimitive(any)
            is Char -> JsonPrimitive(any)
            is Number -> JsonPrimitive(any)
            is Boolean -> JsonPrimitive(any)
            else -> JsonHandler.jsonProcessor.toJsonTree(any)
        }
    }

}