package org.hkyaxhfg.bootstrap.springfox

import springfox.documentation.spring.web.plugins.Docket

/**
 * @author: wjf
 * @date: 2021/12/28
 *
 * @see [SpringfoxDefiner] Springfox定义器, 可以自定义接口文档, 默认的实现为 [DefaultSpringfoxDefiner].
 */
fun interface SpringfoxDefiner {

    /**
     * @see [definition] 定义接口文档.
     *
     * @return [Docket] 返回一个接口文档定义
     */
    fun definition(): Docket
}