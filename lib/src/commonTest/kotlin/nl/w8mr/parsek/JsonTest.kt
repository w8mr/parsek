package nl.w8mr.parsek

import io.kotest.matchers.shouldBe
import nl.w8mr.parsek.text.*
import kotlin.test.Test
import nl.w8mr.parsek.text.any
import nl.w8mr.parsek.text.or

class JsonTest {
    @Test
    fun `basic json`() {
        val parser = object {
            val ws = any(' ' or '\t' or '\n')
            val quotesString = '"' and any(char { it != '"'}) and '"'
            val key = ws and quotesString and ws
            val properties = key and ':' and ref(::value) sepBy ','
            val obj: Parser<Char, Map<String, Any>> = ('{' and properties and '}').map { it.toMap() }
            val value = ws and oneOf(obj, quotesString) and ws
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