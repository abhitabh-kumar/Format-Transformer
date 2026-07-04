package com.jsonformatter.converter.xml

import com.jsonformatter.constants.FormatIds
import com.jsonformatter.converter.Converter
import com.jsonformatter.parser.JsonNode
import com.jsonformatter.util.XmlUtils

class XmlConverter : Converter {

    override val formatId: String = FormatIds.XML

    override val formatName: String = "XML"

    override val extension: String = "xml"

    override fun convert(node: JsonNode): String = buildString {
        append("""<?xml version="1.0" encoding="UTF-8"?>""")
        append('\n')
        appendNode("root", node, 0)
    }.trimEnd()

    private fun StringBuilder.appendNode(tagName: String, node: JsonNode, indent: Int) {
        val safeTag = XmlUtils.toSafeTagName(tagName)
        when (node) {
            JsonNode.NullValue -> appendEmptyElement(safeTag, indent)
            is JsonNode.BooleanValue -> appendSimpleElement(safeTag, node.value.toString(), indent)
            is JsonNode.NumberValue -> appendSimpleElement(safeTag, node.value, indent)
            is JsonNode.StringValue -> appendSimpleElement(safeTag, node.value, indent)
            is JsonNode.ArrayNode -> node.elements.forEach { element ->
                appendNode(safeTag, element, indent)
            }
            is JsonNode.ObjectNode -> {
                appendIndent(indent)
                append('<').append(safeTag).append('>').append('\n')
                node.properties.forEach { (key, value) ->
                    appendNode(key, value, indent + 2)
                }
                appendIndent(indent)
                append('<').append('/').append(safeTag).append('>').append('\n')
            }
        }
    }

    private fun StringBuilder.appendSimpleElement(tagName: String, value: String, indent: Int) {
        appendIndent(indent)
        append('<').append(tagName).append('>')
        append(XmlUtils.escape(value))
        append('<').append('/').append(tagName).append('>').append('\n')
    }

    private fun StringBuilder.appendEmptyElement(tagName: String, indent: Int) {
        appendIndent(indent)
        append('<').append(tagName).append("/>").append('\n')
    }

    private fun StringBuilder.appendIndent(indent: Int) {
        repeat(indent) { append(' ') }
    }
}
