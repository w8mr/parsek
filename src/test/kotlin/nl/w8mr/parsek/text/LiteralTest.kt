package nl.w8mr.parsek.text

import nl.w8mr.parsek.ParseException
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class LiteralTest {
    @Test
    fun simpel() {
        val parser = literal("keyword")
        assertEquals(Unit, parser.parse("keyword 532"))
    }

    @Test
    fun partial() {
        val parser = literal("keyword")
        try {
            parser.parse("key 532")
        } catch (pe: ParseException) {
            assertEquals("keyword not found at position 0 (key 532)", pe.error.message)
        }
    }

    @Test
    fun partialErrorLevel3() {
        val parser = literal("keyword")
        try {
            parser.parse("keyring 532", errorLevel = 3)
        } catch (pe: ParseException) {
            assertEquals("partial match for keyword, key matched, expected character w but found character r at position 3 (ring 532)", pe.error.message)
        }
    }

    @Test
    fun noMatchErrorLevel3() {
        val parser = literal("keyword")
        try {
            parser.parse("word 532", errorLevel = 3)
        } catch (pe: ParseException) {
            assertEquals("keyword not found at position 0 (word 532)", pe.error.message)
        }
    }

}