package nl.w8mr.parsek

import io.kotest.matchers.shouldBe
import nl.w8mr.parsek.text.*
import kotlin.test.Test
import nl.w8mr.parsek.text.zeroOrMore
import or

class JsonTest {
    @Test
    fun `basic json`() {
        val parser = object {
            val ws = zeroOrMore(' ' or '\t' or '\n')
            val quotesString = seq(char('"'), zeroOrMore(char { it != '"'}),  char("")) { _, s, _ -> s }//val quotesString = '"' and zeroOrMore(char { it != '"'}) and '"'
            val key = quotesString
            val obj: Parser<Char, Any> = combi {
                -ws
                -char('{')
                val properties = -zeroOrMore(seq(seq(seq(ws, key, ws, char(":")) { _, k, _, _ -> k } , seq(ws, ref(::value), ws) { _, v, _ -> v }), ws, repeat(char(','),1,0), ws) { p, _, _, _ -> p})
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