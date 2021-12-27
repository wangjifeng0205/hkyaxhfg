package org.hkyaxhfg.auth

import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import kotlin.reflect.KClass

/**
 * @author: wjf
 * @date: 2021/12/24 16:17
 *
 * @see [AuthAnnotatedResolver] 鉴权注解解析器, 所有的鉴权注解都应该继承此类.
 * @see [Info] 鉴权信息, 从 [Auth.get] 函数返回.
 * @see [AuthAnnotation] 鉴权解析器对应的注解.
 */
abstract class AuthAnnotatedResolver<Info, AuthAnnotation : Annotation>(private val auth: Auth<Info>, private val authAnnotationClass: KClass<out AuthAnnotation>) : HandlerMethodArgumentResolver {

    /**
     * @see [getInfo] 获取鉴权信息.
     *
     * @param [authAnnotation] 此鉴权解析器要解析的对应的注解
     *
     * @return [Info] 鉴权信息
     */
    abstract fun getInfo(authAnnotation: AuthAnnotation, auth: Auth<Info> = this.auth): Info?

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(authAnnotationClass.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        return getInfo(validationAuthAnnotation(parameter))
    }

    /**
     * @see [validationAuthAnnotation] 对存在 [AuthAnnotation] 的参数进行校验.
     *
     * @return [parameter] 参数
     *
     * @return [AuthAnnotation] 鉴权注解
     */
    private fun validationAuthAnnotation(parameter: MethodParameter) : AuthAnnotation {
        return parameter.getParameterAnnotation(authAnnotationClass.java) ?: throw AuthException("${authAnnotationClass.qualifiedName} is null")
    }

}