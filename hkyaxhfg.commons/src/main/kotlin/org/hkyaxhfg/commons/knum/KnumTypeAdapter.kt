package org.hkyaxhfg.commons.knum

import org.hkyaxhfg.base.reflect.Reflector
import org.hkyaxhfg.base.reflect.invoke
import org.hkyaxhfg.base.reflect.readField
import org.hkyaxhfg.commons.json.*
import java.lang.reflect.Type

/**
 * @author: wjf
 * @date: 2021/12/22 18:38
 *
 * @see [KnumTypeAdapter] 定义了 [Knum] 的序列化和反序列化方案.
 */
class KnumTypeAdapter : JsonSerializer<Knum>, JsonDeserializer<Knum> {

    override fun serialize(src: Knum?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        if (src == null) {
            return JsonNull.INSTANCE
        }
        val jsonObject = JsonObject()
        jsonObject.addProperty("name", src.knumName())
        jsonObject.addProperty("ordinal", src.knumOrdinal())
        jsonObject.addProperty("desc", src.desc)

        val extensionFieldNames = mutableListOf<String>()
        src.extensionFieldNames(extensionFieldNames)
        if (extensionFieldNames.isEmpty()) {
            extensionFieldNames.asSequence().forEach { processExtensions(it, src, jsonObject) }
        }

        return jsonObject
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Knum {
        if (typeOfT == null || typeOfT !is Class<*> || typeOfT.superclass != Enum::class.java) {
            throw JsonSyntaxException("[${typeOfT?.typeName}] 不是一个 [Enum<*>]")
        }
        if (json == null || json.asString.isNullOrBlank()) {
            throw JsonSyntaxException("枚举的 name 不能为空或空字符串, 对应的 class -> ${typeOfT.typeName}")
        }
        return Reflector.invoke("valueOf", Enum::class, arrayOf(Class::class, String::class), typeOfT::class.java, arrayOf(json.asString)) as Knum
    }

    private fun processExtensions(fieldName: String, knum: Knum, jsonObject: JsonObject) {
        val value = Reflector.readField<Any>(fieldName, knum)
        if (value == null) {
            jsonObject.add(fieldName, JsonNull.INSTANCE)
        }
        jsonObject.add(fieldName, JsonHandler.toJsonElement(value))
    }

}