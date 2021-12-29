package org.hkyaxhfg.commons.knum

import org.hkyaxhfg.base.reflect.Reflector
import org.hkyaxhfg.base.reflect.invoke
import org.reflections.Reflections
import org.reflections.ReflectionsException
import org.reflections.scanners.Scanners
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import org.reflections.util.FilterBuilder
import org.springframework.beans.factory.InitializingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

/**
 * @author: wjf
 * @date: 2021/12/27
 *
 * @see [KnumAutoConfiguration] Knum的自动配置.
 */
@Configuration
@EnableConfigurationProperties(KnumProperties::class)
@ConditionalOnBean(value = [KnumProperties::class], name = ["knumProperties"])
class KnumAutoConfiguration(val knumProperties: KnumProperties) : InitializingBean, KnumFinder {

    private val knumMap = mutableMapOf<String, List<Knum>>()

    override fun afterPropertiesSet() {
        if (knumProperties.enabled) {
            scans().forEach {
                reset(it)
            }
        }
    }

    override fun finding(): Map<String, List<Knum>> {
        return knumMap
    }

    override fun findByName(name: String): List<Knum> {
        return knumMap[name] ?: listOf()
    }

    /**
     * @see [scans] 扫描包.
     *
     * @return [List] [Reflections] 反射实体
     */
    private fun scans(): List<Reflections> {
        val scans = knumProperties.scans
        if (scans.isBlank()) {
            return listOf()
        }
        return scans.splitToSequence(",").map { it.trim() }.filter { it.isNotBlank() }.map {
            return@map Reflections(
                    ConfigurationBuilder()
                            .setUrls(ClasspathHelper.forPackage(it))
                            .filterInputsBy(FilterBuilder().includePattern("${it}.*"))
                            .setScanners(Scanners.SubTypes)
            )
        }.toList()
    }

    /**
     * @see [reset] 设置knum实例列表.
     *
     * @param [reflections] 反射实体
     */
    private fun reset(reflections: Reflections) {
        try {
            val enumSet = reflections.getSubTypesOf(Enum::class.java)
            if (enumSet.isEmpty()) {
                return
            }
            enumSet.filter {
                it.interfaces.contains(Knum::class.java)
            }.forEach {
                val knumList = Reflector.invoke(Knum.VALUES, Enum::class, arrayOf(), null, arrayOf())
                @Suppress("UNCHECKED_CAST")
                knumMap[it.simpleName] = (knumList as Array<Knum>).toList()
            }
        } catch (e: ReflectionsException) {
            return
        }
    }


}