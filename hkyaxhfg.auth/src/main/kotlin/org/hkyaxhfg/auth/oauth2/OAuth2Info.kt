package org.hkyaxhfg.auth.oauth2

/**
 * @author: wjf
 * @date: 2021/12/24 17:45
 *
 * @see [OAuth2Info] 符合OAuth2协议的应用必须继承此类.
 *
 * @param [appId] 应用id
 * @param [appSecret] 应用id对应的密匙
 */
abstract class OAuth2Info(private val appId: String, private val appSecret: String)