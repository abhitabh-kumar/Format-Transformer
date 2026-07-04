package com.jsonformatter.converter.properties

import com.jsonformatter.constants.FormatIds
import com.jsonformatter.converter.Converter
import com.jsonformatter.parser.JsonNode
import com.jsonformatter.util.JsonTreeFlattener

class PropertiesConverter : Converter {

    override val formatId: String = FormatIds.PROPERTIES

    override val formatName: String = "application.properties"

    override val extension: String = "properties"

    override fun convert(node: JsonNode): String =
        JsonTreeFlattener.flatten(node)
            .joinToString(separator = "\n") { (key, value) -> "$key=$value" }
}
