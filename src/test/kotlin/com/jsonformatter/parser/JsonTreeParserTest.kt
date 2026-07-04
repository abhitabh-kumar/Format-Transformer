package com.jsonformatter.parser

import com.google.gson.JsonParser
import com.google.gson.Strictness
import com.google.gson.stream.JsonReader
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.StringReader

class JsonTreeParserTest {

    private val parser = JsonTreeParser()

    @Test
    fun `parses nested object into json node tree`() {
        val element = parseStrict("""{"database":{"host":"localhost"}}""")
        val node = parser.parse(element)
        assertTrue(node is JsonNode.ObjectNode)
        val database = (node as JsonNode.ObjectNode).properties["database"]
        assertTrue(database is JsonNode.ObjectNode)
    }

    private fun parseStrict(json: String) =
        JsonReader(StringReader(json)).apply { strictness = Strictness.STRICT }.let(JsonParser::parseReader)
}
