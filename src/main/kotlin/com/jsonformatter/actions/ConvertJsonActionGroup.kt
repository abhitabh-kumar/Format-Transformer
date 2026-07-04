package com.jsonformatter.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.project.Project
import com.jsonformatter.converter.Converter
import com.jsonformatter.factory.ConverterFactory
import com.jsonformatter.notification.NotificationService
import com.jsonformatter.service.FormatConversionService

class ConvertJsonActionGroup : DefaultActionGroup("Convert JSON", true) {

    init {
        ConverterFactory.getAllConverters().forEach { converter ->
            add(ConvertJsonAction(converter))
        }
    }
}

class ConvertJsonAction(
    private val converter: Converter,
) : AnAction(converter.formatName) {

    override fun actionPerformed(event: AnActionEvent) {
        val project: Project = event.project ?: return
        try {
            FormatConversionService.getInstance(project).convertActiveEditorJson(converter.formatId)
        } catch (exception: Exception) {
            NotificationService.getInstance().showUnexpectedError(
                project,
                exception.message ?: "Failed to convert JSON to ${converter.formatName}.",
            )
        }
    }

    override fun update(event: AnActionEvent) {
        val project = event.project
        event.presentation.isEnabledAndVisible = project != null
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT
}
