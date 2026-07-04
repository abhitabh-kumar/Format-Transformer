package com.jsonformatter.converter.java

import com.jsonformatter.testutil.TestJsonNodes
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class JavaPropertiesConverterTest {

    private val converter = JavaPropertiesConverter()

    @Test
    fun `converts flattened json to java properties code`() {
        val json = """{ "database": { "host": "localhost" } }"""
        val result = converter.convert(TestJsonNodes.parse(json))
        assertTrue(result.contains("Properties props = new Properties();"))
        assertTrue(result.contains("""props.setProperty("database.host", "localhost");"""))
    }
}
