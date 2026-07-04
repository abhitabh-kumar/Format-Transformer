package com.jsonformatter.parser

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.Strictness
import com.google.gson.stream.JsonReader
import java.io.StringReader

class JsonParserService {

    fun parse(jsonContent: String): JsonElement {
        val reader = JsonReader(StringReader(jsonContent.trim()))
        reader.strictness = Strictness.STRICT
        return JsonParser.parseReader(reader)
    }
}
