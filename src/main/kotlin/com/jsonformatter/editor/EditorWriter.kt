package com.jsonformatter.editor

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.jsonformatter.util.JsonUtils
import java.io.IOException

class EditorWriter {

    fun openInNewEditorTab(
        project: Project,
        content: String,
        sourceFile: VirtualFile?,
        defaultBasename: String,
        extension: String,
    ): EditorWriteResult {
        return try {
            WriteCommandAction.runWriteCommandAction<EditorWriteResult>(project) {
                val outputDirectory = resolveOutputDirectory(project, sourceFile)
                    ?: return@runWriteCommandAction EditorWriteResult.Failure(
                        EditorWriteError.NO_OUTPUT_DIRECTORY,
                        IOException("Could not determine where to save the converted file."),
                    )

                val outputFilename = JsonUtils.resolveOutputFilename(
                    sourceFilename = sourceFile?.name,
                    defaultBasename = defaultBasename,
                    extension = extension,
                )
                val outputFile = outputDirectory.findChild(outputFilename)
                    ?: outputDirectory.createChildData(project, outputFilename)

                VfsUtil.saveText(outputFile, content)
                FileEditorManager.getInstance(project).openFile(outputFile, true)
                EditorWriteResult.Success(outputFile)
            } ?: EditorWriteResult.Failure(
                EditorWriteError.UNEXPECTED_ERROR,
                IOException("Could not open converted output in the editor."),
            )
        } catch (exception: IOException) {
            EditorWriteResult.Failure(EditorWriteError.IO_ERROR, exception)
        } catch (exception: Exception) {
            EditorWriteResult.Failure(EditorWriteError.UNEXPECTED_ERROR, exception)
        }
    }

    private fun resolveOutputDirectory(project: Project, sourceFile: VirtualFile?): VirtualFile? {
        val parentDirectory = sourceFile?.parent
        if (parentDirectory != null && parentDirectory.isValid && parentDirectory.exists()) {
            return parentDirectory
        }

        val projectBasePath = project.basePath ?: return null
        return LocalFileSystem.getInstance().findFileByPath(projectBasePath)
    }
}

sealed class EditorWriteResult {
    data class Success(val virtualFile: VirtualFile) : EditorWriteResult()

    data class Failure(
        val error: EditorWriteError,
        val cause: Throwable? = null,
    ) : EditorWriteResult()
}

enum class EditorWriteError {
    IO_ERROR,
    NO_OUTPUT_DIRECTORY,
    UNEXPECTED_ERROR,
}
