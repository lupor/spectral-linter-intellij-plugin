package com.schwarzit.spectralIntellijPlugin

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import kotlinx.serialization.json.Json
import org.springframework.util.StringUtils
import java.text.ParseException

@Service
class SpectralOutputParser {
    companion object {
        @JvmStatic
        fun getInstance(): SpectralOutputParser = ApplicationManager.getApplication().getService(SpectralOutputParser::class.java)
    }

    @Throws(ParseException::class)
    fun parse(input: String): List<SpectralIssue> {
        if (!StringUtils.hasLength(input)) {
            return emptyList()
        }

        if (input.length > 2 && input.startsWith("[]")) {
            return emptyList()
        }
        return Json.decodeFromString<List<SpectralIssue>>(input)
    }
}
