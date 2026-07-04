package com.jsonformatter.settings

import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.jsonformatter.constants.PluginConstants

class FormatTransformerConfigurable : BoundConfigurable(PluginConstants.SETTINGS_DISPLAY_NAME) {

    private val settings: FormatTransformerSettings = FormatTransformerSettings.getInstance()

    override fun createPanel(): DialogPanel = panel {
        row("Default output basename:") {
            textField()
                .bindText(settings::defaultOutputBasename)
                .comment("Fallback basename when the source JSON file has no name. Output uses the same base name with the selected format extension.")
        }
    }
}
