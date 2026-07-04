package com.jsonformatter.converter.java

import com.jsonformatter.testutil.TestJsonNodes
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class JavaMapConverterTest {

    private val converter = JavaMapConverter()

    @Test
    fun `converts flattened json to java map code`() {
        val json = """{ "database": { "host": "localhost", "port": 3306 } }"""
        val result = converter.convert(TestJsonNodes.parse(json))
        assertTrue(result.contains("Map<String, Object> map = new LinkedHashMap<>();"))
        assertTrue(result.contains("""map.put("database.host", "localhost");"""))
        assertTrue(result.contains("""map.put("database.port", 3306);"""))
    }
}
