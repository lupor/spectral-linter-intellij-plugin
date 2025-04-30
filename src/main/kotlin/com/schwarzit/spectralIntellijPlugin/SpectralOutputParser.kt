package com.schwarzit.spectralIntellijPlugin

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import kotlinx.serialization.json.Json
import java.text.ParseException

@Service
class SpectralOutputParser {
    companion object {
        @JvmStatic
        fun getInstance(): SpectralOutputParser = ApplicationManager.getApplication().getService(SpectralOutputParser::class.java)
    }

    @Throws(ParseException::class)
    fun parse(input: String): List<SpectralIssue> {
        return Json.decodeFromString<List<SpectralIssue>>(input)
    }
}
