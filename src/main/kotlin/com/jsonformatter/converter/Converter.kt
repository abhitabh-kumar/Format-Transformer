package com.jsonformatter.converter

import com.jsonformatter.parser.JsonNode

interface Converter {

    val formatId: String

    val formatName: String

    val extension: String

    fun convert(node: JsonNode): String
}
