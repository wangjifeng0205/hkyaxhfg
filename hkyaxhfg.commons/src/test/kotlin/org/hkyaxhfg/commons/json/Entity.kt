package org.hkyaxhfg.commons.json

/**
 * @author: wjf
 * @date: 2021/12/21 13:26
 *
 * 测试使用的entity.
 */

class User() {

    var username: String? = null
    var age: Int? = null
    var others: List<User>? = null

    constructor(username: String?, age: Int?, others: List<User>?): this() {
        this.username = username
        this.age = age
        this.others = others
    }

    override fun toString(): String {
        return "User(username=$username, age=$age, others=$others)"
    }

}