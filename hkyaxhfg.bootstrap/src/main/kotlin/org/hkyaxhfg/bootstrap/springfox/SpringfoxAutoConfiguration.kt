package org.hkyaxhfg.bootstrap.springfox

import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.InitializingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.schema.ModelSpecification
import springfox.documentation.schema.ScalarModelSpecification
import springfox.documentation.service.*
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import java.net.InetAddress
import javax.annotation.Resource

/**
 * @author: wjf
 * @date: 2021/12/28
 *
 * @see [SpringfoxAutoConfiguration] Springfox的自定配置.
 */
@Configuration
@EnableConfigurationProperties(SpringfoxProperties::class)
@ConditionalOnBean(value = [SpringfoxProperties::class], name = ["springfoxProperties"])
class SpringfoxAutoConfiguration(val springfoxProperties: SpringfoxProperties) : InitializingBean, SpringfoxDefiner {

    @Resource
    private lateinit var environment: Environment

    override fun afterPropertiesSet() {
        if (springfoxProperties.enabled) {
            definition()
        }
    }

    override fun definition(): Docket {
        return Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .globalRequestParameters(parameters())
                .ignoredParameterTypes(*ignoredParameterTypes())
                .forCodeGeneration(true)
                .consumes(setOf("application/x-www-form-urlencoded"))
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation::class.java))
                .paths(PathSelectors.any())
                .build()
    }

    private fun apiInfo(): ApiInfo {

        return ApiInfoBuilder()
                .title(springfoxProperties.docTitle)
                .description(springfoxProperties.docDesc)
                .termsOfServiceUrl("http://${InetAddress.getLocalHost().hostAddress}:${environment["server.port"]}${environment["server.servlet.context-path"]}/swagger-ui/index.html")
                .contact(Contact(springfoxProperties.contactorName, springfoxProperties.contactUrl, springfoxProperties.contactorEmail))
                .version("3.0.0")
                .build()
    }

    private fun parameters(): List<RequestParameter> {
        val requestParameters = springfoxProperties.globalRequestParameters
        return requestParameters.map {
            return@map RequestParameter(
                    it.name,
                    it.parameterType,
                    it.desc,
                    it.required,
                    false,
                    false,
                    ParameterSpecification(SimpleParameterSpecification(
                            null, null, null, null, null, null,
                            ModelSpecification(null, null, ScalarModelSpecification(it.dataType),
                                    null, null, null, null), null
                    ), null),
                    null, null, 1, null, 1
            )
        }.toList()
    }

    private fun ignoredParameterTypes(): Array<Class<*>> {
        return emptyArray()
    }

}