package com.jsonformatter.util

object CsvUtils {

    fun escape(value: String): String {
        val mustQuote = value.contains(',') || value.contains('"') || value.contains('\n') || value.contains('\r')
        return if (mustQuote) {
            "\"${value.replace("\"", "\"\"")}\""
        } else {
            value
        }
    }
}
