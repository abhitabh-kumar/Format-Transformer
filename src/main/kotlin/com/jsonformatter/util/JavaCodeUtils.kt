package com.jsonformatter.util

object JavaCodeUtils {

    fun stringLiteral(value: String): String =
        "\"${value.replace("\\", "\\\\").replace("\"", "\\\"")}\""

    fun valueLiteral(value: String): String =
        when {
            value.equals("true", ignoreCase = true) -> "Boolean.TRUE"
            value.equals("false", ignoreCase = true) -> "Boolean.FALSE"
            value.isEmpty() -> "null"
            value.matches(NUMBER_PATTERN) -> value
            else -> stringLiteral(value)
        }

    private val NUMBER_PATTERN = Regex("^-?\\d+(\\.\\d+)?$")
}
