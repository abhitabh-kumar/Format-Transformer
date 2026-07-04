package com.jsonformatter.validator

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class JsonValidatorTest {

    private val validator = JsonValidator()

    @Test
    fun `returns valid for well formed json`() {
        val result = validator.validate("""{"key":"value"}""")

        assertTrue(result is ValidationResult.Valid)
    }

    @Test
    fun `returns invalid for malformed json`() {
        val result = validator.validate("""{"key": value}""")

        assertTrue(result is ValidationResult.Invalid)
        assertEquals(ValidationError.INVALID_JSON, (result as ValidationResult.Invalid).error)
    }

    @Test
    fun `returns invalid for empty content`() {
        val result = validator.validate("   ")

        assertTrue(result is ValidationResult.Invalid)
        assertEquals(ValidationError.EMPTY_FILE, (result as ValidationResult.Invalid).error)
    }

    @Test
    fun `returns valid for json arrays`() {
        val result = validator.validate("""[1, 2, 3]""")

        assertTrue(result is ValidationResult.Valid)
    }

    @Test
    fun `returns valid for nested json structures`() {
        val result = validator.validate("""{"users":[{"name":"John"}]}""")

        assertTrue(result is ValidationResult.Valid)
    }

    @Test
    fun `returns invalid for properties style content`() {
        val result = validator.validate("database.host=localhost")

        assertTrue(result is ValidationResult.Invalid)
        assertEquals(ValidationError.INVALID_JSON, (result as ValidationResult.Invalid).error)
    }

    @Test
    fun `returns invalid for json fragment without braces`() {
        val result = validator.validate(""""key": "value"""")

        assertTrue(result is ValidationResult.Invalid)
        assertEquals(ValidationError.INVALID_JSON, (result as ValidationResult.Invalid).error)
    }

    @Test
    fun `returns invalid for trailing content after json document`() {
        val result = validator.validate("""{"key":"value"} trailing""")

        assertTrue(result is ValidationResult.Invalid)
        assertEquals(ValidationError.INVALID_JSON, (result as ValidationResult.Invalid).error)
    }
}
