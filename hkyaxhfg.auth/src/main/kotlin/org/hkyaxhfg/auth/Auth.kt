package org.hkyaxhfg.auth

/**
 * @author: wjf
 * @date: 2021/12/23 16:17
 *
 * @see [Auth] 鉴权接口, 一些鉴权相关的基本操作.
 *
 * @param [Info] 代表承载鉴权信息的实体
 */
fun interface Auth<Info> {

    /**
     * @see [get] 获取鉴权信息.
     *
     * @return [Info] 鉴权信息
     */
    fun get(): Info?

}