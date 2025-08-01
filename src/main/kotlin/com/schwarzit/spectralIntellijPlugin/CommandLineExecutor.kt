package com.schwarzit.spectralIntellijPlugin

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.*
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.util.Key
import java.time.Duration

@Service
class CommandLineExecutor {
    companion object {
        private val logger = getLogger()

        @JvmStatic
        fun getInstance(): CommandLineExecutor =
            ApplicationManager.getApplication().getService(CommandLineExecutor::class.java)
    }

    fun execute(commandLine: GeneralCommandLine, timeout: Duration? = null): ProcessOutput {
        val command = commandLine.commandLineString
        val process = commandLine.createProcess()
        val handler = OSProcessHandler(process, command, Charsets.UTF_8)
        val output = ProcessOutput()

        logger.debug("Executing command: $command")

        handler.addProcessListener(object : ProcessListener {
            override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
                when (outputType) {
                    ProcessOutputType.STDERR -> output.appendStderr(event.text)
                    ProcessOutputType.STDOUT -> output.appendStdout(event.text)
                }
            }
        })

        handler.startNotify()

        val ended = when (timeout) {
            null -> handler.waitFor()
            else -> handler.waitFor(timeout.toMillis())
        }

        if (ended) {
            logger.debug("Command successfully executed with exit code ${output.exitCode}")
            output.exitCode = process.exitValue()
        } else {
            logger.debug("Command timed out after ${timeout?.toMillis()} ms")
            handler.destroyProcess()
            output.setTimeout()
        }

        return output
    }

}
