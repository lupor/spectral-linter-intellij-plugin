package com.schwarzit.spectralIntellijPlugin

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class SpectralOutputParserTest {
    @Test
    @DisplayName("should handle malformed JSON gracefully if there are no issues")
    fun malformedEmptyResultTest() {
        val spectralOutputParser = SpectralOutputParser()
        val input = "[]There is no issue"
        val result = spectralOutputParser.parse(input)
        assertTrue(result.isEmpty(), "should return an empty list")
    }

    @Test
    @DisplayName("should handle empty input gracefully")
    fun emptyInputTest() {
        val spectralOutputParser = SpectralOutputParser()
        val input = ""
        val result = spectralOutputParser.parse(input)
        assertTrue(result.isEmpty(), "should return an empty list")
    }
}