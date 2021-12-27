package org.hkyaxhfg.commons.knum

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/**
 * @author: wjf
 * @date: 2021/12/27
 *
 * @see [KnumProperties] Knum的自动配置属性.
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "hkyaxhfg.knum")
data class KnumProperties(
    /**
     * 是否启用knum收集器
     */
    var enabled: Boolean = false,
    /**
     * knum所在的包名, 多个包则使用[,]隔开
     */
    var scans: String = "*"
)
