package com.jsonformatter.service

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.jsonformatter.converter.Converter
import com.jsonformatter.editor.EditorReader
import com.jsonformatter.editor.EditorReadError
import com.jsonformatter.editor.EditorReadResult
import com.jsonformatter.editor.EditorWriteError
import com.jsonformatter.editor.EditorWriter
import com.jsonformatter.editor.EditorWriteResult
import com.jsonformatter.dialog.ValidationDialogService
import com.jsonformatter.factory.ConverterFactory
import com.jsonformatter.notification.NotificationService
import com.jsonformatter.parser.JsonParserService
import com.jsonformatter.parser.JsonTreeParser
import com.jsonformatter.settings.FormatTransformerSettings
import com.jsonformatter.validator.JsonValidator
import com.jsonformatter.validator.ValidationError
import com.jsonformatter.validator.ValidationResult
import com.jsonformatter.validator.errorMessage

@Service(Service.Level.PROJECT)
class FormatConversionService(private val project: Project) {

    private val editorReader = EditorReader()
    private val editorWriter = EditorWriter()
    private val jsonValidator = JsonValidator()
    private val jsonParserService = JsonParserService()
    private val jsonTreeParser = JsonTreeParser()
    private val notificationService = NotificationService.getInstance()
    private val settings = FormatTransformerSettings.getInstance()

    fun convertActiveEditorJson(formatId: String) {
        val converter = ConverterFactory.getConverter(formatId)
        if (converter == null) {
            notificationService.showUnsupportedFormat(project, formatId)
            return
        }

        when (val readResult = editorReader.readActiveEditorContent(project)) {
            is EditorReadResult.Success -> processJsonContent(
                jsonContent = readResult.content,
                sourceFile = readResult.sourceFile,
                converter = converter,
            )
            is EditorReadResult.Failure -> handleReadFailure(readResult)
        }
    }

    private fun processJsonContent(jsonContent: String, sourceFile: VirtualFile?, converter: Converter) {
        when (val validationResult = jsonValidator.validate(jsonContent)) {
            ValidationResult.Valid -> performConversion(jsonContent, sourceFile, converter)
            is ValidationResult.Invalid -> handleValidationFailure(validationResult)
        }
    }

    private fun performConversion(jsonContent: String, sourceFile: VirtualFile?, converter: Converter) {
        try {
            val jsonElement = jsonParserService.parse(jsonContent)
            val jsonNode = jsonTreeParser.parse(jsonElement)
            val convertedContent = converter.convert(jsonNode)

            when (
                val writeResult = editorWriter.openInNewEditorTab(
                    project = project,
                    content = convertedContent,
                    sourceFile = sourceFile,
                    defaultBasename = settings.defaultOutputBasename,
                    extension = converter.extension,
                )
            ) {
                is EditorWriteResult.Success -> notificationService.showConversionSuccessful(project, converter.formatName)
                is EditorWriteResult.Failure -> handleWriteFailure(writeResult)
            }
        } catch (exception: Exception) {
            notificationService.showUnexpectedError(
                project,
                exception.message ?: "An unexpected error occurred during conversion.",
            )
        }
    }

    private fun handleReadFailure(readResult: EditorReadResult.Failure) {
        when (readResult.error) {
            EditorReadError.NO_ACTIVE_EDITOR -> notificationService.showNoActiveEditor(project)
            EditorReadError.EMPTY_FILE -> notificationService.showEmptyFile(project)
            EditorReadError.IO_ERROR -> notificationService.showIoError(
                project,
                readResult.cause?.message ?: "Failed to read editor content.",
            )
            EditorReadError.UNEXPECTED_ERROR -> notificationService.showUnexpectedError(
                project,
                readResult.cause?.message ?: "Failed to read editor content.",
            )
        }
    }

    private fun handleValidationFailure(validationResult: ValidationResult.Invalid) {
        when (validationResult.error) {
            ValidationError.EMPTY_FILE -> {
                notificationService.showEmptyFile(project)
                ValidationDialogService.showValidationError(
                    project = project,
                    error = ValidationError.EMPTY_FILE,
                    detailMessage = validationResult.errorMessage(),
                )
            }
            ValidationError.INVALID_JSON -> {
                notificationService.showInvalidJson(project, validationResult.errorMessage())
                ValidationDialogService.showValidationError(
                    project = project,
                    error = ValidationError.INVALID_JSON,
                    detailMessage = validationResult.errorMessage(),
                )
            }
        }
    }

    private fun handleWriteFailure(writeResult: EditorWriteResult.Failure) {
        when (writeResult.error) {
            EditorWriteError.NO_OUTPUT_DIRECTORY -> notificationService.showIoError(
                project,
                writeResult.cause?.message
                    ?: "Save the JSON file to disk or open a project before converting.",
            )
            EditorWriteError.IO_ERROR -> notificationService.showIoError(
                project,
                writeResult.cause?.message ?: "Failed to write converted output.",
            )
            EditorWriteError.UNEXPECTED_ERROR -> notificationService.showUnexpectedError(
                project,
                writeResult.cause?.message ?: "Failed to write converted output.",
            )
        }
    }

    companion object {
        fun getInstance(project: Project): FormatConversionService =
            project.getService(FormatConversionService::class.java)
    }
}
