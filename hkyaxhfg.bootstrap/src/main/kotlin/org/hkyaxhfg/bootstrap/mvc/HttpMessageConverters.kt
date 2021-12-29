package org.hkyaxhfg.bootstrap.mvc

import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation
import org.hkyaxhfg.commons.json.JsonHandler
import org.hkyaxhfg.commons.knum.Knum
import org.hkyaxhfg.commons.knum.KnumFinder
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.GsonHttpMessageConverter
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.annotation.Resource

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

@ConditionalOnBean(value = [KnumFinder::class], name = ["knumFinder"])
@RestController
@RequestMapping("/knum")
@Api(tags = ["knum"])
sealed class KnumController {

    @Resource
    private lateinit var knumFinder: KnumFinder

    @ApiOperation(value = "查询所有的knum", httpMethod = "GET")
    @RequestMapping("/findAll")
    suspend fun findAll(): Map<String, List<Knum>> {
        return knumFinder.finding()
    }

    @ApiImplicitParams(
            ApiImplicitParam(name = "name", value = "knum类名", required = true, paramType = "query", dataTypeClass = String::class)
    )
    @RequestMapping("/findAll")
    suspend fun findOne(name: String): List<Knum> {
        return knumFinder.findByName(name)
    }

}