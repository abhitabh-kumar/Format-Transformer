package com.jsonformatter.converter.java

import com.jsonformatter.constants.FormatIds
import com.jsonformatter.converter.Converter
import com.jsonformatter.parser.JsonNode
import com.jsonformatter.util.JavaCodeUtils
import com.jsonformatter.util.JsonTreeFlattener

class JavaMapConverter : Converter {

    override val formatId: String = FormatIds.JAVA_MAP

    override val formatName: String = "Java Map"

    override val extension: String = "java"

    override fun convert(node: JsonNode): String = buildString {
        appendLine("Map<String, Object> map = new LinkedHashMap<>();")
        JsonTreeFlattener.flatten(node).forEach { (key, value) ->
            append("map.put(")
            append(JavaCodeUtils.stringLiteral(key))
            append(", ")
            append(JavaCodeUtils.valueLiteral(value))
            appendLine(");")
        }
    }.trimEnd()
}
