package nl.w8mr.parsek

import nl.w8mr.parsek.text.*
import kotlin.test.Test

class JsonTest {
/*
    @Test
    fun `basic json`() {
        val whitespace = some(oneOf(char(' '),char('\t'),char('\n')))
        val quotesString = seq(char('"'), zeroOrMore(char { it != '"'}),  char("")) { _, s, _ -> s.joinToString("") }
        val key = quotesString
        val obj = combi {
            -whitespace
            char('{')
            val properties = zeroOrMore(seq(key, char(":"), whitespace, value) { k, _, _, v -> k to v } )
        }
        val example="""
            {
                "s": "string",
                "b": true,
                "n": 5,
                "o": {
                    "s": "string"
                }
            }
        """.trimIndent()
    }
*/
}