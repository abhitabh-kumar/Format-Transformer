package com.jsonformatter.converter.text

import com.jsonformatter.testutil.TestJsonNodes
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TextConverterTest {

    private val converter = TextConverter()

    @Test
    fun `converts flattened json to spaced text lines`() {
        val json = """{ "database": { "host": "localhost" } }"""
        assertEquals("database.host = localhost", converter.convert(TestJsonNodes.parse(json)))
    }
}
