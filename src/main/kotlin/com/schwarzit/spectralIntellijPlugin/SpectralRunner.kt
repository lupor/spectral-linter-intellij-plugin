package com.schwarzit.spectralIntellijPlugin

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import com.schwarzit.spectralIntellijPlugin.settings.ProjectSettingsState
import kotlinx.serialization.SerializationException
import java.io.File
import java.io.IOException
import java.text.ParseException
import java.time.Duration
import java.util.*
import java.util.concurrent.ExecutionException

@Service(Service.Level.PROJECT)
class SpectralRunner(private val project: Project) {
    companion object {
        private val logger = getLogger()
        private val timeout = Duration.ofSeconds(30)
    }

    private val executor = project.service<CommandLineExecutor>()
    private val parser = project.service<SpectralOutputParser>()
    private val settings = project.service<ProjectSettingsState>()

    fun run(document: Document): List<SpectralIssue> {
        val file = FileDocumentManager.getInstance().getFile(document)?.toNioPath()?.toFile()
        var tempFile: File? = null
        if (file == null || !settings.useFileOverrides) {
            tempFile = try {
                // It is possible, but unlikely that the editor file is null. In which case we create a temporary one
                File.createTempFile("spectral-intellij-input-", ".tmp").apply { writeText(document.text) }
            } catch (e: IOException) {
                throw SpectralException("Failed to create temporary file", e)
            }
        }

        val fileForAnalysis = tempFile?.absolutePath ?: file!!.absolutePath
        return try {
            getCommand(settings, fileForAnalysis)
                .execute(fileForAnalysis)
        } finally {
            if (tempFile != null && !tempFile.delete()) {
                logger.debug("Failed to delete temporary file ${tempFile.canonicalPath}")
            }
        }
    }

    private fun getCommand(
        settings: ProjectSettingsState,
        filePath: String
    ): GeneralCommandLine {
        var commandString = "-r ${settings.ruleset} -f json lint $filePath"
        if (SystemInfo.isWindows && settings.useNodePackageWin) {
            commandString = "cmd /c spectral $commandString"
        } else if (SystemInfo.isWindows) {
            commandString = "spectral.exe $commandString"
        } else {
            commandString = "spectral $commandString"
        }
        val command = commandString.split(" ")
        val commandLine = GeneralCommandLine(command[0])
            .withParameters(command.drop(1))
            .withWorkDirectory(project.basePath)
            .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)

        val commandEnv = mapOf("NODE_OPTIONS" to "--no-warnings")
        val cleanEnv = mapOf("PATH" to commandLine.parentEnvironment["PATH"]) + commandEnv
        commandLine.withEnvironment(cleanEnv)

        return commandLine
    }

    @Throws(SpectralException::class)
    private fun GeneralCommandLine.execute(filePath: String): List<SpectralIssue> {
        val silentErrors = mapOf(
            "Cannot read properties of null (reading 'type')" to "There is an empty field in your spec",
            "Cannot read properties of null" to "One of your custom rules in your ruleset may have a null reference",
            "Error running Spectral" to "Something unexpected happened. Validate ruleset and yaml file structure"
        )
        val output = try {
            executor.execute(this, timeout)
        } catch (e: ExecutionException) {
            throw SpectralException("Failed to execute command", e)
        }

        val successExitCodes = (0..2).toList()

        if (output.exitCode !in successExitCodes) {
            throw SpectralException("Spectral finished with exit code ${output.exitCode} but expected one of $successExitCodes\n${output.stderr}")
        }
        val hasSilentError: Boolean = output.stderr.isNotBlank() && silentErrors.keys.any { output.stderr.contains(it) }
        if (output.stderr.isNotBlank() && !hasSilentError) {
            throw SpectralException("An unexpected error occurred:\n${output.stderr}")
        }


        if (hasSilentError) {
            val key = silentErrors.keys.first { output.stderr.contains(it) }
            val message = silentErrors[key] ?: "A silent error occurred: {${output.stderr}}"
            return listOf(
                SpectralIssue(
                    "parser-error",
                    emptyList(),
                    message, 0, filePath, ErrorRange(ErrorPosition(0, 0), ErrorPosition(0, 1))
                )
            )
        }

        try {
            return parser.parse(output.stdout)
        } catch (ex: Exception) {
            when (ex) {
                is ParseException, is SerializationException -> {
                    throw SpectralException("Failed to parse output", ex)
                }

                else -> throw ex
            }
        }

    }
}