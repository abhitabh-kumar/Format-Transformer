package com.jsonformatter.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import com.jsonformatter.constants.PluginConstants

@Service(Service.Level.APP)
@State(
    name = "FormatTransformerSettings",
    storages = [Storage("format-transformer.xml")],
)
class FormatTransformerSettings : PersistentStateComponent<FormatTransformerSettings.State> {

    data class State(
        var defaultOutputBasename: String = PluginConstants.DEFAULT_OUTPUT_FILENAME,
        var welcomeShown: Boolean = false,
    )

    private var state = State()

    var defaultOutputBasename: String
        get() = state.defaultOutputBasename
        set(value) {
            state.defaultOutputBasename = value.ifBlank { PluginConstants.DEFAULT_OUTPUT_FILENAME }
        }

    var welcomeShown: Boolean
        get() = state.welcomeShown
        set(value) {
            state.welcomeShown = value
        }

    override fun getState(): State = state

    override fun loadState(state: State) {
        XmlSerializerUtil.copyBean(state, this.state)
    }

    companion object {
        fun getInstance(): FormatTransformerSettings =
            ApplicationManager.getApplication().getService(FormatTransformerSettings::class.java)
    }
}
