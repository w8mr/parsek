package nl.w8mr.parsek.text

import nl.w8mr.parsek.ParseException
import kotlin.test.Test
import kotlin.test.assertEquals

internal class RegexTest {
    @Test
    fun number() {
        val parser = regex("\\d+")
        assertEquals("123", parser.parse("123"))
    }

    @Test
    fun numberPartial() {
        val parser = regex("\\d{5}")
        try {
            parser.parse("123abc", errorLevel = 3)
        } catch (pe: ParseException) {
            assertEquals("Partial match for pattern \\d{5}, 123 matched, failed for character a at position 3 (abc)", pe.error.message)
        }
    }


    @org.junit.jupiter.api.Test
    fun simpel() {
        val parser = regex("regex")
        assertEquals("regex", parser.parse("regex 532"))
    }

    @org.junit.jupiter.api.Test
    fun partial() {
        val parser = regex("regex")
        try {
            parser.parse("regular expression 532")
        } catch (pe: ParseException) {
            assertEquals("Expected pattern regex to match at position 0 (regular ex)", pe.error.message)
        }
    }

    @org.junit.jupiter.api.Test
    fun partialErrorLevel3() {
        val parser = regex("regex")
        try {
            parser.parse("regular expression 532", errorLevel = 3)
        } catch (pe: ParseException) {
            assertEquals("Partial match for pattern regex, reg matched, failed for character u at position 3 (ular expre)", pe.error.message)
        }
    }

    @org.junit.jupiter.api.Test
    fun noMatchErrorLevel3() {
        val parser = regex("regex")
        try {
            parser.parse("no regex 532", errorLevel = 3)
        } catch (pe: ParseException) {
            assertEquals("Expected pattern regex to match at position 0 (no regex 5)", pe.error.message)
        }
    }

}
