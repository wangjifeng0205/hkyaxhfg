package org.hkyaxhfg.bootstrap.springfox

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import springfox.documentation.schema.ScalarType
import springfox.documentation.service.ParameterType

/**
 * @author: wjf
 * @date: 2021/12/28
 *
 * @see [SpringfoxProperties] springfox的自动配置属性.
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "hkyaxhfg.springfox")
data class SpringfoxProperties(
    /**
     * 是否启动文档, 默认为true
     */
    var enabled: Boolean = true,
    /**
     * 文档标题, 默认为 ${spring.application.name}
     */
    var docTitle: String = "",
    /**
     * 文档描述, 默认为 ${spring.application.name}
     */
    var docDesc: String = "",
    /**
     * 联系人名称, 默认为 ${spring.application.name}
     */
    var contactorName: String = "",
    /**
     * 联系网址, 默认为 https://hkyaxhfg.org
     */
    var contactUrl: String = "https://hkyaxhfg.org",
    /**
     * 联系人邮箱, 默认为 hkyaxhfg@gmail.com
     */
    var contactorEmail: String = "hkyaxhfg@gmail.com",
    /**
     * 全局参数
     */
    var globalRequestParameters: List<RequestParameter> = mutableListOf<RequestParameter>()
) {

    /**
     * 请求参数
     */
    inner class RequestParameter {
        /**
         * 参数名称
         */
        var name: String? = null
        /**
         * 参数类型
         */
        var parameterType: ParameterType? = null
        /**
         * 参数描述
         */
        var desc: String? = null
        /**
         * 参数是否必传
         */
        var required: Boolean? = null
        /**
         * 参数数据类型
         */
        var dataType: ScalarType? = null
    }

}
