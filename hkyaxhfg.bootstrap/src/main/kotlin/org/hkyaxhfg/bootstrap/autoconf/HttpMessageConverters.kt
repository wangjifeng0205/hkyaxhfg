package org.hkyaxhfg.bootstrap.autoconf

import org.hkyaxhfg.commons.json.JsonHandler
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.GsonHttpMessageConverter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * @author: wjf
 * @date: 2021/12/23 16:00
 *
 * 关于webmvc相关的自动配置.
 */

typealias JsonHttpMessageConverter = GsonHttpMessageConverter

interface HkyaxhfgMVCConfigurer: WebMvcConfigurer {

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        converters.add(JsonHttpMessageConverter(JsonHandler.jsonProcessor))
    }

}