package com.jsonformatter.notification

import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import com.jsonformatter.constants.PluginConstants

class NotificationService {

    fun showConversionSuccessful(project: Project, formatName: String) {
        showNotification(
            project = project,
            title = "Conversion Successful",
            content = "JSON has been converted to $formatName format.",
            type = NotificationType.INFORMATION,
        )
    }

    fun showInvalidJson(project: Project, detailMessage: String) {
        showNotification(
            project = project,
            title = "Wrong JSON Format",
            content = detailMessage,
            type = NotificationType.ERROR,
        )
    }

    fun showEmptyFile(project: Project) {
        showNotification(
            project = project,
            title = "Empty File",
            content = "The active editor is empty. Open a JSON file with content before converting.",
            type = NotificationType.WARNING,
        )
    }

    fun showNoActiveEditor(project: Project) {
        showNotification(
            project = project,
            title = "No Active Editor",
            content = "Open a JSON file in the editor before running the conversion.",
            type = NotificationType.WARNING,
        )
    }

    fun showUnsupportedFormat(project: Project, formatId: String) {
        showNotification(
            project = project,
            title = "Unsupported Format",
            content = "The selected output format is not supported: $formatId",
            type = NotificationType.ERROR,
        )
    }

    fun showIoError(project: Project, message: String) {
        showNotification(
            project = project,
            title = "I/O Error",
            content = message,
            type = NotificationType.ERROR,
        )
    }

    fun showUnexpectedError(project: Project, message: String) {
        showNotification(
            project = project,
            title = "Unexpected Error",
            content = message,
            type = NotificationType.ERROR,
        )
    }

    fun showWelcome(project: Project, onDismiss: () -> Unit) {
        val notification = createNotification(
            project = project,
            title = "Welcome to Format Transformer",
            content = buildWelcomeMessage(),
            type = NotificationType.INFORMATION,
        )

        notification.addAction(
            NotificationAction.createSimple("Got it") {
                onDismiss()
                notification.expire()
            },
        )

        notification.notify(project)
    }

    private fun buildWelcomeMessage(): String = """
        How to use:
        1. Open JSON
        2. Right Click
        3. Convert JSON → Select Format
        4. View the converted file in a new editor tab
    """.trimIndent()

    private fun showNotification(
        project: Project,
        title: String,
        content: String,
        type: NotificationType,
    ) {
        createNotification(project, title, content, type).notify(project)
    }

    private fun createNotification(
        project: Project?,
        title: String,
        content: String,
        type: NotificationType,
    ): Notification =
        NotificationGroupManager.getInstance()
            .getNotificationGroup(PluginConstants.NOTIFICATION_GROUP_ID)
            .createNotification(title, content, type)

    companion object {
        @JvmStatic
        fun getInstance(): NotificationService = NotificationService()
    }
}
