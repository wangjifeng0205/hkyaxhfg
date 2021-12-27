package org.hkyaxhfg.auth.userauth

/**
 * @author: wjf
 * @date: 2021/12/23 16:22
 *
 * @see [UserAuth] 用户鉴权注解, 获取当前登录用户的用户信息.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention
@Repeatable
@MustBeDocumented
annotation class UserAuth(
    /**
     * @see [userNullable] 用户信息是否可以为null, 默认为false.
     */
    val userNullable: Boolean = false
)