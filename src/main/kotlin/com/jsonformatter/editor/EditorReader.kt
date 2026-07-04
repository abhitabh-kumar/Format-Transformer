package com.jsonformatter.editor

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import java.io.IOException

class EditorReader {

    fun readActiveEditorContent(project: Project): EditorReadResult {
        val editor = getActiveEditor(project)
            ?: return EditorReadResult.Failure(EditorReadError.NO_ACTIVE_EDITOR)

        return readEditorContent(editor)
    }

    private fun getActiveEditor(project: Project): Editor? =
        FileEditorManager.getInstance(project).selectedTextEditor

    private fun readEditorContent(editor: Editor): EditorReadResult {
        return try {
            val content = editor.document.text
            if (content.isBlank()) {
                EditorReadResult.Failure(EditorReadError.EMPTY_FILE)
            } else {
                EditorReadResult.Success(
                    content = content,
                    sourceFile = editor.virtualFile,
                )
            }
        } catch (exception: IOException) {
            EditorReadResult.Failure(EditorReadError.IO_ERROR, exception)
        } catch (exception: Exception) {
            EditorReadResult.Failure(EditorReadError.UNEXPECTED_ERROR, exception)
        }
    }
}

sealed class EditorReadResult {
    data class Success(
        val content: String,
        val sourceFile: VirtualFile?,
    ) : EditorReadResult()

    data class Failure(
        val error: EditorReadError,
        val cause: Throwable? = null,
    ) : EditorReadResult()
}

enum class EditorReadError {
    NO_ACTIVE_EDITOR,
    EMPTY_FILE,
    IO_ERROR,
    UNEXPECTED_ERROR,
}
