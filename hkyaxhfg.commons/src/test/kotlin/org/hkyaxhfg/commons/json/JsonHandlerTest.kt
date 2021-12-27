package org.hkyaxhfg.commons.json

import org.junit.Test

/**
 * @author: wjf
 * @date: 2021/12/21 13:23
 *
 * JsonHandler测试类.
 */
class JsonHandlerTest {

    @Test
    fun fromTest() {
        val user = User("wjf", 18, listOf())
        println(JsonHandler.fromObj(user))

        val json = Json()
        json.addObject("user", user)
        println(JsonHandler.fromJson(json))
    }

    @Test
    fun toTest() {
        val json = """
            {
              "username" : "wjf",
              "age" : 18,
              "others" : [ ]
            }
        """.trimIndent()
        println(JsonHandler.toObj<User>(json).toString())
    }
}