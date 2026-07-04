package com.jsonformatter.converter.properties

import com.jsonformatter.testutil.TestJsonNodes
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PropertiesConverterTest {

    private val converter = PropertiesConverter()

    @Test
    fun `converts nested objects to dot notation properties`() {
        val json = """
            {
              "database": {
                "host": "localhost",
                "port": 3306
              },
              "debug": true
            }
        """.trimIndent()

        val expected = """
            database.host=localhost
            database.port=3306
            debug=true
        """.trimIndent()

        assertEquals(expected, converter.convert(TestJsonNodes.parse(json)))
    }

    @Test
    fun `converts arrays using bracket index notation`() {
        val json = """
            {
              "users": [
                { "name": "John" },
                { "name": "Alex" }
              ]
            }
        """.trimIndent()

        val expected = """
            users[0].name=John
            users[1].name=Alex
        """.trimIndent()

        assertEquals(expected, converter.convert(TestJsonNodes.parse(json)))
    }

    @Test
    fun `converts null values to empty assignment`() {
        val json = """{ "optional": null }"""
        assertEquals("optional=", converter.convert(TestJsonNodes.parse(json)))
    }
}
