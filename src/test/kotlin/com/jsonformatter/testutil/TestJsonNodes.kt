package com.jsonformatter.testutil

import com.jsonformatter.parser.JsonNode
import com.jsonformatter.parser.JsonParserService
import com.jsonformatter.parser.JsonTreeParser

object TestJsonNodes {

    private val jsonParserService = JsonParserService()
    private val jsonTreeParser = JsonTreeParser()

    fun parse(json: String): JsonNode =
        jsonTreeParser.parse(jsonParserService.parse(json))
}
