package org.hkyaxhfg.auth.userauth

import org.hkyaxhfg.auth.Auth
import org.hkyaxhfg.auth.AuthAnnotatedResolver
import org.hkyaxhfg.auth.AuthException

/**
 * @author: wjf
 * @date: 2021/12/24 15:49
 *
 * @see [UserAuthAnnotatedResolver] 注解 [UserAuth] 的注解解析器.
 */
class UserAuthAnnotatedResolver<UserInfo>(private val userAuth: Auth<UserInfo>) : AuthAnnotatedResolver<UserInfo, UserAuth>(userAuth, UserAuth::class) {

    override fun getInfo(authAnnotation: UserAuth, auth: Auth<UserInfo>): UserInfo? {
        val userInfo = userAuth.get()

        if (!authAnnotation.userNullable && userInfo == null) {
            throw AuthException("用户未登录")
        }

        return userInfo
    }

}