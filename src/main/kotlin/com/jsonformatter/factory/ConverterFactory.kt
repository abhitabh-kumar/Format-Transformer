package com.jsonformatter.factory

import com.jsonformatter.converter.Converter
import com.jsonformatter.converter.csv.CsvConverter
import com.jsonformatter.converter.java.JavaMapConverter
import com.jsonformatter.converter.java.JavaPropertiesConverter
import com.jsonformatter.converter.markdown.MarkdownConverter
import com.jsonformatter.converter.properties.PropertiesConverter
import com.jsonformatter.converter.text.TextConverter
import com.jsonformatter.converter.xml.XmlConverter
import com.jsonformatter.converter.yaml.YamlConverter

object ConverterFactory {

    private val converters: Map<String, Converter> = registerConverters()

    fun getConverter(formatId: String): Converter? =
        converters[formatId]

    fun getAllConverters(): List<Converter> =
        converters.values.toList()

    fun isSupported(formatId: String): Boolean =
        converters.containsKey(formatId)

    private fun registerConverters(): Map<String, Converter> =
        listOf(
            PropertiesConverter(),
            YamlConverter(),
            XmlConverter(),
            TextConverter(),
            CsvConverter(),
            MarkdownConverter(),
            JavaMapConverter(),
            JavaPropertiesConverter(),
        ).associateBy(Converter::formatId)
}
