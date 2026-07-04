package com.jsonformatter.dialog

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.jsonformatter.validator.ValidationError

object ValidationDialogService {

    fun showValidationError(project: Project?, error: ValidationError, detailMessage: String) {
        val title = when (error) {
            ValidationError.EMPTY_FILE -> "Empty File"
            ValidationError.INVALID_JSON -> "Wrong JSON Format"
        }

        val message = when (error) {
            ValidationError.EMPTY_FILE -> """
                The active editor is empty.
                
                Open a JSON file with content before converting.
            """.trimIndent()

            ValidationError.INVALID_JSON -> """
                The selected content is not valid JSON.
                
                Please fix the syntax and try again.
                
                Details: $detailMessage
            """.trimIndent()
        }

        Messages.showErrorDialog(project, message, title)
    }
}
