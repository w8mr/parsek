package nl.w8mr.parsek

import io.kotest.matchers.shouldBe
import nl.w8mr.parsek.text.*
import kotlin.test.Test
import nl.w8mr.parsek.text.any
import nl.w8mr.parsek.text.or
import nl.w8mr.parsek.text.some
import nl.w8mr.parsek.text.and
import kotlin.js.JsName


class JsonTest {
    infix fun Long.power(exponentVal: Int): Long = when {
        exponentVal != 0 -> this * (this power (exponentVal - 1))
        else -> 1L
    }

    @Test
    @JsName("basicJson")
    fun `basic json`() {
        val parser = object {
            private infix fun Long.power(exponentVal: Long): Long = when {
                exponentVal != 0L -> this * (this power (exponentVal - 1))
                else -> 1L
            }

            private fun parseIntWithExponnents(it: String) =
                it.split(Regex("[Ee]")).let { it[0].toLong() * (10L power (it.getOrNull(1)?.toLong() ?: 0)) }

            val ws = any(' ' or '\t' or '\n' or '\r')
            val element = ws and ref(::value) and ws
            val json = element
            val elements = element sepBy ','
            val array = ('[' and elements and ']') or
                    ('[' and ws and ']').value(emptyList())
            val _string = '"' and (char() until '"')
            val key = ws and _string and ws
            val member = key and ':' and element
            val members = member sepBy ','
            val obj = ('{' and members and '}').map { it.toMap() } or
                    ('{' and ws and '}').value(emptyMap())
            val _true = "true" value true
            val _false = "false" value false
            val _null = "null" value null
            val onenine = char { it in '1'..'9'}
            val digit = char('0') or onenine
            val digits = some(digit)
            val integer = (onenine and digits) or
                    digit or
                    ('-' and onenine and digits) or
                    ('-' and digit)
            val fraction = char('.') and digits
            val sign = repeat(char('+') or char('-'), 1, 0).map { it.firstOrNull() ?: "" }
            val exponent = repeat(char('E') or char('e') and sign and digits, 1, 0).map { it.firstOrNull() ?: "" }
            val number = (integer and fraction and exponent).map { it.toDouble()  } or
                    (integer and exponent).map { parseIntWithExponnents(it) }
            val value: Parser<Char, Any?> = oneOf(obj, array, _string, number, _true, _false, _null)
        }
        val example="""
            {
                "s": "string",
                "o": {
                    "s": "string"
                },
                "oe": { },
                "a": [ "string" ],
                "ae": [ ],
                "t": true,
                "f": false,
                "n": null,
                "i": 123456789012,
                "ie" : 12e2,
                "d": 12.34,
                "de": 12.34e2
            }
        """.trimIndent()
        parser.json(example) shouldBe mapOf(
            "s" to "string",
            "o" to mapOf("s" to "string"),
            "oe" to emptyMap<String, String>(),
            "a" to listOf("string"),
            "ae" to emptyList<Nothing>(),
            "t" to true,
            "f" to false,
            "n" to null,
            "i" to 123456789012L,
            "ie" to 1200L,
            "d" to 12.34,
            "de" to 1234.0
        ) as Any
    }
}