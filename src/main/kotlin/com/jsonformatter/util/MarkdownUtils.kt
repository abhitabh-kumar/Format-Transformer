package com.jsonformatter.util

object MarkdownUtils {

    fun escapeCell(value: String): String =
        value.replace("|", "\\|").replace("\n", " ")
}
