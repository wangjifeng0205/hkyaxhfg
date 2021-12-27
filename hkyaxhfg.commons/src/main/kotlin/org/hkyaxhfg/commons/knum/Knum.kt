package org.hkyaxhfg.commons.knum

import org.hkyaxhfg.commons.json.JsonSyntaxException

/**
 * @author: wjf
 * @date: 2021/12/22 14:56
 *
 * @see [Knum] 针对枚举的一些额外的自定义规范.
 */
interface Knum {

    companion object {
        /**
         * @see [VALUES] Enum对应的方法签名.
         */
        const val VALUES = "values"
    }

    /**
     * @see [desc] 枚举对应的描述.
     */
    val desc: String

    /**
     * @see [knumName] 返回 [Knum] 的名称.
     *
     * @return [String] [Knum] 的名称
     */
    fun knumName(): String? = (this as? Enum<*>)?.name

    /**
     * @see [knumOrdinal] 返回 [Knum] 在定义时的排序.
     *
     * @return [Int] [Knum] 在定义时的排序
     */
    fun knumOrdinal(): Int? = (this as? Enum<*>)?.ordinal

    /**
     * @see [extensionFieldNames] 扩展的字段名称, 此字段名称必须字段定义在枚举中.
     */
    fun extensionFieldNames(fieldNames: MutableList<String>)

    /**
     * @see [values] 获取此 [Knum] 对应的主 [Knum] 的所有子 [Knum].
     *
     * @return [List] [Knum] 获取此 [Knum] 对应的主 [Knum] 的所有子 [Knum]
     */
    fun values(): List<Knum> {
        if (this !is Enum<*>) {
            throw JsonSyntaxException("[${this::class.qualifiedName}] 不是一个 [Enum<*>]")
        }
        val enum = this as Enum<*>
        val mainClass = enum::class.java.declaringClass
        val enumConstants = mainClass.enumConstants
        return enumConstants.asSequence().map { it as Knum }.toList()
    }
}