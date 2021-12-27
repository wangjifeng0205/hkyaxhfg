package org.hkyaxhfg.base.reflect

import kotlin.reflect.KClass

/**
 * @author: wjf
 * @date: 2021/12/20 18:06
 *
 * @see [Reflector] 扩展的Reflector接口的一些方法, 用来处理Kotlin反射相关操作.
 */
object Reflector

/**
 * @see [new] 创建一个对象 [T], 必须拥有无参构造器.
 *
 * @return [T] T
 */
inline fun <reified T : Any> Reflector.new(): T = newInstance<T>(arrayOf(), arrayOf())

/**
 * @see [newFromClass] 创建一个对象 [T], 必须拥有无参构造器.
 *
 * @param [kClass] 类型 [T] 的字节码对象
 *
 * @return [T] T
 */
fun <T : Any> Reflector.newFromClass(kClass: KClass<T>): T = newInstanceFromClass<T>(kClass, arrayOf(), arrayOf())

/**
 * @see [newInstance] 创建一个对象 [T], 任意构造器.
 *
 * @param [parameterTypes] 构造器参数类型数组
 * @param [parameters] 构造器参数
 *
 * @return [T] T
 */
inline fun <reified T : Any> Reflector.newInstance(parameterTypes: Array<KClass<*>>, parameters: Array<Any?>): T = newInstanceFromClass(T::class, parameterTypes, parameters)

/**
 * @see [newInstanceFromClass] 创建一个对象 [T], 任意构造器.
 *
 * @param [kClass] 类型 [T] 的字节码对象
 * @param [parameterTypes] 构造器参数类型数组
 * @param [parameters] 构造器参数
 *
 * @return [T] T
 */
fun <T : Any> Reflector.newInstanceFromClass(kClass: KClass<T>, parameterTypes: Array<KClass<*>>, parameters: Array<Any?>): T {
    val classes = parameterTypes.asSequence().map { it.java }.toList().toTypedArray()
    return kClass.java.getConstructor(*classes).newInstance(parameters)
}

/**
 * @see [invoke] 执行方法.
 *
 * @param [methodName] 方法名称
 * @param [targetClass] 方法所属类
 * @param [parameterTypes] 方法的参数列表类型
 * @param [target] 执行方法的对象
 * @param [parameters] 执行方法所需的参数
 *
 * @return [Any] 执行方法之后获得的返回结果
 */
fun Reflector.invoke(methodName: String, targetClass: KClass<*>, parameterTypes: Array<KClass<*>>, target: Any?, parameters: Array<Any?>): Any? {
    return targetClass.java.getMethod(
        methodName,
        *parameterTypes.asSequence().map { it.java }.toList().toTypedArray()
    ).invoke(
        target,
        *parameters
    )
}

/**
 * @see [readField] 读取字段数据.
 *
 * @param [fieldName] 字段名称
 * @param [any] 目标对象
 *
 * @return [T] 字段数据
 */
fun <T> Reflector.readField(fieldName: String, any: Any): T? {
    val jClass = any::class.java
    val field = jClass.getField(fieldName)
    var accessible = field.isAccessible
    if (!accessible) {
        field.isAccessible = true
    }
    val value = field.get(any)
    field.isAccessible = accessible
    @Suppress("UNCHECKED_CAST")
    return value as? T
}