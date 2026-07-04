package com.jsonformatter.converter.java

import com.jsonformatter.constants.FormatIds
import com.jsonformatter.converter.Converter
import com.jsonformatter.parser.JsonNode
import com.jsonformatter.util.JavaCodeUtils
import com.jsonformatter.util.JsonTreeFlattener

class JavaPropertiesConverter : Converter {

    override val formatId: String = FormatIds.JAVA_PROPERTIES

    override val formatName: String = "Java Properties"

    override val extension: String = "java"

    override fun convert(node: JsonNode): String = buildString {
        appendLine("Properties props = new Properties();")
        JsonTreeFlattener.flatten(node).forEach { (key, value) ->
            append("props.setProperty(")
            append(JavaCodeUtils.stringLiteral(key))
            append(", ")
            append(JavaCodeUtils.stringLiteral(value))
            appendLine(");")
        }
    }.trimEnd()
}
