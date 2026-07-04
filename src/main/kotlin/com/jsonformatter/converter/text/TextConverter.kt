package com.jsonformatter.converter.text

import com.jsonformatter.constants.FormatIds
import com.jsonformatter.converter.Converter
import com.jsonformatter.parser.JsonNode
import com.jsonformatter.util.JsonTreeFlattener

class TextConverter : Converter {

    override val formatId: String = FormatIds.TEXT

    override val formatName: String = "Text"

    override val extension: String = "txt"

    override fun convert(node: JsonNode): String =
        JsonTreeFlattener.flatten(node)
            .joinToString(separator = "\n") { (key, value) -> "$key = $value" }
}
