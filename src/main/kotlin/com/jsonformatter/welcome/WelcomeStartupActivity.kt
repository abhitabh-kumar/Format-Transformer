package com.jsonformatter.welcome

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.jsonformatter.notification.NotificationService
import com.jsonformatter.settings.FormatTransformerSettings

class WelcomeStartupActivity : ProjectActivity {

    override suspend fun execute(project: Project) {
        val settings = FormatTransformerSettings.getInstance()

        synchronized(lock) {
            if (settings.welcomeShown) {
                return
            }
        }

        NotificationService.getInstance().showWelcome(project) {
            settings.welcomeShown = true
        }
    }

    companion object {
        private val lock = Any()
    }
}
