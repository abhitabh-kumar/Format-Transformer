package com.jsonformatter.util

import com.jsonformatter.parser.JsonNode

object JsonTreeFlattener {

    fun flatten(node: JsonNode, prefix: String = ""): List<Pair<String, String>> =
        when (node) {
            JsonNode.NullValue -> listOf(prefix to "")
            is JsonNode.ObjectNode -> node.properties.flatMap { (key, value) ->
                val propertyKey = JsonUtils.buildPropertyKey(prefix, key)
                flatten(value, propertyKey)
            }
            is JsonNode.ArrayNode -> node.elements.flatMapIndexed { index, element ->
                val propertyKey = JsonUtils.buildArrayPropertyKey(prefix, index)
                flatten(element, propertyKey)
            }
            is JsonNode.BooleanValue -> listOf(prefix to node.value.toString())
            is JsonNode.NumberValue -> listOf(prefix to node.value)
            is JsonNode.StringValue -> listOf(prefix to node.value)
        }

    fun flattenValue(node: JsonNode): String =
        when (node) {
            JsonNode.NullValue -> ""
            is JsonNode.BooleanValue -> node.value.toString()
            is JsonNode.NumberValue -> node.value
            is JsonNode.StringValue -> node.value
            is JsonNode.ArrayNode -> node.elements.joinToString(", ") { flattenValue(it) }
            is JsonNode.ObjectNode -> node.properties.entries.joinToString(", ") { (key, value) ->
                "$key=${flattenValue(value)}"
            }
        }
}
