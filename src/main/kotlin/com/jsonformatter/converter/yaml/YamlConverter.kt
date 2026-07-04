package com.jsonformatter.converter.yaml

import com.jsonformatter.constants.FormatIds
import com.jsonformatter.converter.Converter
import com.jsonformatter.parser.JsonNode

class YamlConverter : Converter {

    override val formatId: String = FormatIds.YAML

    override val formatName: String = "YAML"

    override val extension: String = "yaml"

    override fun convert(node: JsonNode): String =
        buildString { appendYamlNode(node, 0) }.trimEnd()

    private fun StringBuilder.appendYamlNode(node: JsonNode, indent: Int) {
        when (node) {
            JsonNode.NullValue -> append("null")
            is JsonNode.BooleanValue -> append(node.value.toString())
            is JsonNode.NumberValue -> append(node.value)
            is JsonNode.StringValue -> append(node.value)
            is JsonNode.ArrayNode -> appendYamlArray(node, indent)
            is JsonNode.ObjectNode -> appendYamlObject(node, indent)
        }
    }

    private fun StringBuilder.appendYamlObject(node: JsonNode.ObjectNode, indent: Int) {
        node.properties.entries.forEachIndexed { index, (key, value) ->
            if (index > 0) append('\n')
            appendIndent(indent)
            append(safeYamlKey(key))
            append(':')
            when (value) {
                is JsonNode.ObjectNode -> {
                    append('\n')
                    appendYamlObject(value, indent + 2)
                }
                is JsonNode.ArrayNode -> {
                    append(' ')
                    appendYamlArray(value, indent + 2)
                }
                else -> {
                    append(' ')
                    appendYamlNode(value, indent)
                }
            }
        }
    }

    private fun StringBuilder.appendYamlArray(node: JsonNode.ArrayNode, indent: Int) {
        node.elements.forEachIndexed { index, element ->
            if (index > 0) append('\n')
            appendIndent(indent)
            append("- ")
            when (element) {
                is JsonNode.ObjectNode -> {
                    append('\n')
                    element.properties.entries.forEachIndexed { entryIndex, (key, value) ->
                        if (entryIndex > 0) append('\n')
                        appendIndent(indent + 2)
                        append(safeYamlKey(key))
                        append(": ")
                        appendYamlNode(value, indent + 2)
                    }
                }
                is JsonNode.ArrayNode -> appendYamlArray(element, indent + 2)
                else -> appendYamlNode(element, indent)
            }
        }
    }

    private fun StringBuilder.appendIndent(indent: Int) {
        repeat(indent) { append(' ') }
    }

    private fun safeYamlKey(key: String): String =
        if (key.any { it.isWhitespace() || it == ':' || it == '#' }) {
            "\"${key.replace("\"", "\\\"")}\""
        } else {
            key
        }
}
