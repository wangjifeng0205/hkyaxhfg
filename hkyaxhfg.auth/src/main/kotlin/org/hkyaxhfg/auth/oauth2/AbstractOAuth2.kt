package org.hkyaxhfg.auth.oauth2

import org.hkyaxhfg.auth.Auth

/**
 * @author: wjf
 * @date: 2021/12/24 17:44
 *
 * @see [AbstractOAuth2] 抽象的OAuth2协议鉴权实现.
 *
 * @param [OAuth2Info] OAuth2鉴权信息
 */
abstract class AbstractOAuth2<OAuth2Info : org.hkyaxhfg.auth.oauth2.OAuth2Info>
    : Auth<OAuth2Info>