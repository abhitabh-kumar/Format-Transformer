package com.jsonformatter.converter.xml

import com.jsonformatter.testutil.TestJsonNodes
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class XmlConverterTest {

    private val converter = XmlConverter()

    @Test
    fun `converts nested object to xml`() {
        val json = """
            {
              "database": {
                "host": "localhost"
              }
            }
        """.trimIndent()

        val result = converter.convert(TestJsonNodes.parse(json))
        assertTrue(result.contains("""<?xml version="1.0" encoding="UTF-8"?>"""))
        assertTrue(result.contains("<database>"))
        assertTrue(result.contains("<host>localhost</host>"))
    }
}
