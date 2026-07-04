package com.jsonformatter.util

object XmlUtils {

    fun escape(value: String): String =
        value.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;")

    fun toSafeTagName(name: String): String {
        val sanitized = name.replace(Regex("[^A-Za-z0-9_.-]"), "_")
        return if (sanitized.firstOrNull()?.isDigit() == true) {
            "item_$sanitized"
        } else {
            sanitized.ifBlank { "item" }
        }
    }
}
