package com.jsonformatter.converter.markdown

import com.jsonformatter.testutil.TestJsonNodes
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MarkdownConverterTest {

    private val converter = MarkdownConverter()

    @Test
    fun `converts flattened json to markdown table`() {
        val json = """{ "database": { "host": "localhost" } }"""
        val result = converter.convert(TestJsonNodes.parse(json))
        assertTrue(result.contains("| Key | Value |"))
        assertTrue(result.contains("| database.host | localhost |"))
    }
}
