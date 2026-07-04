package com.jsonformatter.converter.markdown

import com.jsonformatter.constants.FormatIds
import com.jsonformatter.converter.Converter
import com.jsonformatter.parser.JsonNode
import com.jsonformatter.util.JsonTreeFlattener
import com.jsonformatter.util.MarkdownUtils

class MarkdownConverter : Converter {

    override val formatId: String = FormatIds.MARKDOWN

    override val formatName: String = "Markdown"

    override val extension: String = "md"

    override fun convert(node: JsonNode): String = buildString {
        appendLine("| Key | Value |")
        appendLine("| --- | --- |")
        JsonTreeFlattener.flatten(node).forEach { (key, value) ->
            appendLine("| ${MarkdownUtils.escapeCell(key)} | ${MarkdownUtils.escapeCell(value)} |")
        }
    }.trimEnd()
}
