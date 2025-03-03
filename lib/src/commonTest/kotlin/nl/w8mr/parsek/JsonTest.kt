package nl.w8mr.parsek

import io.kotest.matchers.shouldBe
import nl.w8mr.parsek.text.*
import kotlin.test.Test
import nl.w8mr.parsek.text.any
import or

class JsonTest {
    @Test
    fun `basic json`() {
        val parser = object {
            val ws = any(' ' or '\t' or '\n')
            val quotesString = '"' and any(char { it != '"'}) and '"'
            val key = seq(ws, quotesString, ws) { _, k, _-> k }
            val obj: Parser<Char, Any> = combi {
                -ws
                -char('{')
                val properties = -zeroOrMore(seq(seq(key and ':' , seq(ws, ref(::value), ws) { _, v, _ -> v }), ws, repeat(char(','),1,0), ws) { p, _, _, _ -> p})
                -char('}')
                properties.toMap()

            }
            val value = oneOf(obj, quotesString)
        }
        val example="""
            {
                "s": "string",
                "o": {
                    "s": "string"
                }
            }
        """.trimIndent()
        parser.value.parse(example) shouldBe mapOf("s" to "string", "o" to mapOf("s" to "string"))
    }
}