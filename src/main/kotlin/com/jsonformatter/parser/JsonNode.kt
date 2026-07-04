package com.jsonformatter.parser

sealed interface JsonNode {

    data class ObjectNode(val properties: LinkedHashMap<String, JsonNode>) : JsonNode

    data class ArrayNode(val elements: List<JsonNode>) : JsonNode

    data class StringValue(val value: String) : JsonNode

    data class NumberValue(val value: String) : JsonNode

    data class BooleanValue(val value: Boolean) : JsonNode

    data object NullValue : JsonNode
}
