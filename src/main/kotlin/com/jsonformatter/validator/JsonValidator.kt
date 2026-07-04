package com.jsonformatter.validator

import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import com.google.gson.Strictness
import com.google.gson.stream.JsonReader
import com.google.gson.stream.MalformedJsonException
import java.io.IOException
import java.io.StringReader

class JsonValidator {

    fun validate(jsonContent: String): ValidationResult {
        if (jsonContent.isBlank()) {
            return ValidationResult.Invalid(ValidationError.EMPTY_FILE)
        }

        val trimmedContent = jsonContent.trim()
        if (!looksLikeJson(trimmedContent)) {
            return ValidationResult.Invalid(
                error = ValidationError.INVALID_JSON,
                cause = IllegalArgumentException("Content must start with '{' or '[' to be valid JSON."),
            )
        }

        return try {
            val reader = JsonReader(StringReader(trimmedContent))
            reader.strictness = Strictness.STRICT
            JsonParser.parseReader(reader)

            if (reader.peek() != com.google.gson.stream.JsonToken.END_DOCUMENT) {
                return ValidationResult.Invalid(
                    error = ValidationError.INVALID_JSON,
                    cause = IllegalArgumentException("Unexpected content after the JSON document."),
                )
            }

            ValidationResult.Valid
        } catch (exception: JsonParseException) {
            ValidationResult.Invalid(ValidationError.INVALID_JSON, exception)
        } catch (exception: MalformedJsonException) {
            ValidationResult.Invalid(ValidationError.INVALID_JSON, exception)
        } catch (exception: IllegalArgumentException) {
            ValidationResult.Invalid(ValidationError.INVALID_JSON, exception)
        } catch (exception: IOException) {
            ValidationResult.Invalid(ValidationError.INVALID_JSON, exception)
        }
    }

    private fun looksLikeJson(content: String): Boolean =
        content.startsWith("{") || content.startsWith("[")
}

sealed class ValidationResult {
    data object Valid : ValidationResult()

    data class Invalid(
        val error: ValidationError,
        val cause: Throwable? = null,
    ) : ValidationResult()
}

enum class ValidationError {
    EMPTY_FILE,
    INVALID_JSON,
}

fun ValidationResult.Invalid.errorMessage(): String =
    when (error) {
        ValidationError.EMPTY_FILE -> "The active editor is empty."
        ValidationError.INVALID_JSON -> cause?.message?.takeIf { it.isNotBlank() }
            ?: "The selected content is not valid JSON."
    }
