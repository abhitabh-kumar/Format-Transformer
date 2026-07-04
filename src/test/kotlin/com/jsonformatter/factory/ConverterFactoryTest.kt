package com.jsonformatter.factory

import com.jsonformatter.constants.FormatIds
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ConverterFactoryTest {

    @Test
    fun `returns all eight converters`() {
        assertEquals(8, ConverterFactory.getAllConverters().size)
    }

    @Test
    fun `returns properties converter by id`() {
        val converter = ConverterFactory.getConverter(FormatIds.PROPERTIES)
        assertNotNull(converter)
        assertEquals("application.properties", converter?.formatName)
    }

    @Test
    fun `returns null for unknown format`() {
        assertTrue(!ConverterFactory.isSupported("unknown"))
    }
}
