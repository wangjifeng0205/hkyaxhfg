package org.hkyaxhfg.commons.json

import kotlin.test.Test

/**
 * @author: wjf
 * @date: 2021/12/21 13:35
 *
 * JsonTest测试类.
 */
class JsonTest {

    @Test
    fun createObjectNodeTest() {
        val objectJson = Json()
        objectJson.addObject("username", "wjf")
        objectJson.addObject("age", 18)
        objectJson.addObject("others", listOf<User>())
        println(objectJson.toJsonString())
        println("=====================")

        val arrayJson = Json()
        arrayJson.addArray(User("lcw", 20, listOf()))
        arrayJson.addArray(User("lkz", 20, listOf()))
        arrayJson.addArray(User("clm", 20, listOf()))
        println(arrayJson.toJsonString())
        println("=====================")

        val json = Json(objectJson)
        json.addObject("others", arrayJson)
        println(json.toJsonString())
    }

    @Test
    fun createArrayNodeTest() {
        val arrayJson = Json()
        arrayJson.addArray(User("lcw", 20, listOf()))

        val objectJson = Json()
        objectJson.addObject("username", "wjf")
        objectJson.addObject("age", 18)
        objectJson.addObject("others", listOf<User>())

        arrayJson.addArray(objectJson)
        arrayJson.addArray(1)
        arrayJson.addArray("string")
        println(arrayJson.toJsonString())
    }

    @Test
    fun getObjectTest() {
        val objectJson = Json()
        objectJson.addObject("username", "wjf")
        objectJson.addObject("age", 18)
        println(objectJson.toJsonString())
        println("=====================")

        val arrayJson = Json()
        arrayJson.addArray(User("lcw", 20, listOf()))
        arrayJson.addArray(User("lkz", 20, listOf()))
        arrayJson.addArray(User("clm", 20, listOf()))
        println(arrayJson.toJsonString())
        println("=====================")

        val json = Json(objectJson)
        json.addObject("others", arrayJson)
        println(json.toJsonString())

        val any = json.get("#this.others[0].age")
        println(any)
    }

    @Test
    fun getArrayTest() {

        val arrayJson = Json()
        arrayJson.addArray(User("lcw", 20, listOf()))
        arrayJson.addArray(User("lkz", 20, listOf()))
        arrayJson.addArray(User("clm", 20, listOf()))
        println(arrayJson.toJsonString())
        println("=====================")

        val json = Json(arrayJson)
        json.addArray("test")
        println(json.toJsonString())

        val any = json.get("#this[0].username")
        println(any)
    }

}