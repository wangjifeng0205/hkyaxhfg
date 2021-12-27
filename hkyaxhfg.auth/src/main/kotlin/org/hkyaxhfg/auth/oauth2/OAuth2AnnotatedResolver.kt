package org.hkyaxhfg.auth.oauth2

import org.hkyaxhfg.auth.Auth
import org.hkyaxhfg.auth.AuthAnnotatedResolver
import org.hkyaxhfg.auth.AuthException

/**
 * @author: wjf
 * @date: 2021/12/24 17:32
 *
 * @see [OAuth2AnnotatedResolver] 注解 [OAuth2] 的注解解析器.
 */
class OAuth2AnnotatedResolver<OAuth2Info : org.hkyaxhfg.auth.oauth2.OAuth2Info>
    (private val oauth2Auth: Auth<OAuth2Info>) : AuthAnnotatedResolver<OAuth2Info, OAuth2>(oauth2Auth, OAuth2::class) {

    override fun getInfo(authAnnotation: OAuth2, auth: Auth<OAuth2Info>): OAuth2Info? {
        return oauth2Auth.get() ?: throw AuthException("不具有OAuth2访问权限")
    }

}