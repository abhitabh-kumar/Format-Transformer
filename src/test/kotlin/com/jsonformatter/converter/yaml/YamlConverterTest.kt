package com.jsonformatter.converter.yaml

import com.jsonformatter.testutil.TestJsonNodes
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class YamlConverterTest {

    private val converter = YamlConverter()

    @Test
    fun `converts nested object to yaml`() {
        val json = """
            {
              "database": {
                "host": "localhost",
                "port": 3306
              }
            }
        """.trimIndent()

        val result = converter.convert(TestJsonNodes.parse(json))
        assertTrue(result.contains("database:"))
        assertTrue(result.contains("host: localhost"))
        assertTrue(result.contains("port: 3306"))
    }
}
