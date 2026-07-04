package com.jsonformatter.converter.csv

import com.jsonformatter.constants.FormatIds
import com.jsonformatter.converter.Converter
import com.jsonformatter.parser.JsonNode
import com.jsonformatter.util.CsvUtils
import com.jsonformatter.util.JsonTreeFlattener

class CsvConverter : Converter {

    override val formatId: String = FormatIds.CSV

    override val formatName: String = "CSV"

    override val extension: String = "csv"

    override fun convert(node: JsonNode): String = buildString {
        appendLine("Key,Value")
        JsonTreeFlattener.flatten(node).forEach { (key, value) ->
            appendLine("${CsvUtils.escape(key)},${CsvUtils.escape(value)}")
        }
    }.trimEnd()
}
