package com.jsonformatter.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class JsonUtilsOutputFilenameTest {

    @Test
    fun `derives output filename from json source and extension`() {
        assertEquals(
            "application.properties",
            JsonUtils.resolveOutputFilename("application.json", "output", "properties"),
        )
    }

    @Test
    fun `uses default basename when source name is missing`() {
        assertEquals(
            "output.yaml",
            JsonUtils.resolveOutputFilename(null, "output", "yaml"),
        )
    }
}
