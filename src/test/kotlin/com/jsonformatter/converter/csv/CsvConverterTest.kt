package com.jsonformatter.converter.csv

import com.jsonformatter.testutil.TestJsonNodes
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CsvConverterTest {

    private val converter = CsvConverter()

    @Test
    fun `converts flattened json to csv rows`() {
        val json = """
            {
              "database": {
                "host": "localhost"
              }
            }
        """.trimIndent()

        val expected = """
            Key,Value
            database.host,localhost
        """.trimIndent()

        assertEquals(expected, converter.convert(TestJsonNodes.parse(json)))
    }
}
