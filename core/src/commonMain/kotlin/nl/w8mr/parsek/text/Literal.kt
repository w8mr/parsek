package nl.w8mr.parsek.text

import nl.w8mr.parsek.Context
import nl.w8mr.parsek.LiteralParser
import nl.w8mr.parsek.Parser
import nl.w8mr.parsek.simple
import nl.w8mr.parsek.simpleLiteral
import nl.w8mr.parsek.value

fun literal(expected: Char, message: String = "Character {actual} does not meet expected {expected}") = simpleLiteral<Char> {
    when (val ch = token()) {
        expected -> Unit
        else -> fail(message.replace("{actual}", ch.toString()).replace("{expected}", expected.toString()))
    }
}

/**
 *  Matches on the [expected] string and failes with [message] if it succeeds Unit is returned
 *
 * Example of parsing for a string
 * <!--- ZIPDOK include src/commonTest/nl/w8mr/parsek/text/StringTest#string-fixed -->
 * ```kotlin
 *    val parser = string("add")
 *    parser.parse("add 1") shouldBe "add"
 *  ```
 * <!--- ZIPDOK end -->
 */
fun literal(expected: String, message: String = "Character {actual} does not meet expected {expected}, partial match: {partial}") = simpleLiteral<Char> {
    var matched = mutableListOf<Char>()
    for (expectedChar in expected) {
        val ch = token()
        when (ch) {
            expectedChar -> matched += ch
            else -> when {
                matched.isEmpty() -> fail(message
                    .replace("{actual}", ch.toString())
                    .replace("{expected}", expectedChar.toString())
                    .replace("{partial}", "<None>"))
                else -> fail(message
                    .replace("{actual}", ch.toString())
                    .replace("{expected}", expectedChar.toString())
                    .replace("{partial}", matched.joinToString("")))
            }
        }
    }
}

infix fun <R> String.value(value: R) = literal(this).value(value)
infix fun <R> Char.value(value: R) = literal(this).value(value)
