package com.jsonformatter.util

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive

object JsonUtils {

    fun isNull(element: JsonElement): Boolean =
        element.isJsonNull

    fun isObject(element: JsonElement): Boolean =
        element.isJsonObject

    fun isArray(element: JsonElement): Boolean =
        element.isJsonArray

    fun isPrimitive(element: JsonElement): Boolean =
        element.isJsonPrimitive

    fun primitiveToString(primitive: JsonPrimitive): String = when {
        primitive.isBoolean -> primitive.asBoolean.toString()
        primitive.isNumber -> primitive.asNumber.toString()
        primitive.isString -> primitive.asString
        else -> primitive.toString()
    }

    fun buildPropertyKey(prefix: String, key: String): String =
        if (prefix.isEmpty()) key else "$prefix.$key"

    fun buildArrayPropertyKey(prefix: String, index: Int): String =
        "$prefix[$index]"

    fun resolveOutputFilename(sourceFilename: String?, defaultBasename: String, extension: String): String {
        val normalizedExtension = extension.removePrefix(".")
        if (sourceFilename.isNullOrBlank()) {
            return sanitizeFilename("$defaultBasename.$normalizedExtension")
        }

        val baseName = sourceFilename.substringBeforeLast('.').ifBlank { sourceFilename }
        return sanitizeFilename("$baseName.$normalizedExtension")
    }

    private fun sanitizeFilename(filename: String): String =
        filename.replace(Regex("[\\\\/:*?\"<>|]"), "_")
}
