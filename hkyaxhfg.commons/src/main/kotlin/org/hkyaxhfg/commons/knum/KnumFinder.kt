package org.hkyaxhfg.commons.knum

/**
 * @author: wjf
 * @date: 2021/12/27
 *
 * @see [KnumFinder] Knum查找器, 查找扫描包下的Knum.
 */
interface KnumFinder {

    /**
     * @see [finding] 查找所有的Knum映射.
     *
     * @return [Map] Knum映射
     */
    fun finding(): Map<String, List<Knum>>

    /**
     * @see [findByName] 通过Knum的名称查找Knum实例.
     *
     * @param [name] Knum的名称
     *
     * @return [List] Knum实例
     */
    fun findByName(name: String): List<Knum>

}