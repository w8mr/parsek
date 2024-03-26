package nl.w8mr.parsek

import nl.w8mr.parsek.text.Parsers.number
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class AndTest {
    @Test
    fun simple() {
        val parser = number and "abc"
        assertEquals(123, parser.parse("123abc"))
    }

    @Test
    fun fail() {
        val parser = number and "abc"
        try {
            parser.parse("abc123")
        } catch (pe: ParseException) {
            assertEquals("seq first parser failed at position 0 (abc123)", pe.error.message)
            assertEquals("Map failed at position 0 (abc123)", (pe.error.subResults[0] as Parser.Error).message)
            assertEquals(
                "Expected pattern -?\\d+ to match at position 0 (abc123)",
                (pe.error.subResults[0].subResults[0] as Parser.Error).message,
            )
        }
        assertEquals(123, parser.parse("123abc"))
    }
}
