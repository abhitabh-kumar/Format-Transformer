package com.jsonformatter.parser

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.jsonformatter.util.JsonUtils

class JsonTreeParser {

    fun parse(element: JsonElement): JsonNode =
        when {
            JsonUtils.isNull(element) -> JsonNode.NullValue
            JsonUtils.isObject(element) -> parseObject(element.asJsonObject)
            JsonUtils.isArray(element) -> parseArray(element.asJsonArray)
            JsonUtils.isPrimitive(element) -> parsePrimitive(element.asJsonPrimitive)
            else -> JsonNode.NullValue
        }

    private fun parseObject(jsonObject: JsonObject): JsonNode.ObjectNode {
        val properties = LinkedHashMap<String, JsonNode>()
        jsonObject.entrySet().forEach { (key, value) ->
            properties[key] = parse(value)
        }
        return JsonNode.ObjectNode(properties)
    }

    private fun parseArray(jsonArray: JsonArray): JsonNode.ArrayNode =
        JsonNode.ArrayNode(jsonArray.map(::parse))

    private fun parsePrimitive(primitive: JsonPrimitive): JsonNode =
        when {
            primitive.isBoolean -> JsonNode.BooleanValue(primitive.asBoolean)
            primitive.isNumber -> JsonNode.NumberValue(primitive.asNumber.toString())
            primitive.isString -> JsonNode.StringValue(primitive.asString)
            else -> JsonNode.NullValue
        }
}
