package nl.w8mr.parsek.text

import nl.w8mr.parsek.Context
import nl.w8mr.parsek.Parser
import nl.w8mr.parsek.map
import nl.w8mr.parsek.optional
import nl.w8mr.parsek.seq
import nl.w8mr.parsek.simple
import nl.w8mr.parsek.some

/**
 *  Matches on the [expected] character and failes with [message] if it succeeds the character is returned as
 *  a string for easier combining with other porsers
 *
 * Example of parsing for a single character
 * <!--- ZIPDOK include src/commonTest/nl/w8mr/parsek/text/CharTest#char-literal -->
 * ```kotlin
 *    val parser = char('a')
 *    parser("ab") shouldBe "a"
 *  ```
 * <!--- ZIPDOK end -->
 */
fun char(expected: Char, message: String = "Character {actual} does not meet expected {expected}") = simple<Char, String> {
    when (val ch = token()) {
        expected -> ch.toString()
        else -> fail(message.replace("{actual}", ch.toString()).replace("{expected}", expected.toString()))
    }
}

fun char() = simple<Char, String> {
    token().toString()
}

fun char(message: String = "Character {actual} does not meet predicate", predicate: (Char) -> Boolean) = simple<Char, String> {
    val ch = token()
    when {
        predicate(ch) -> ch.toString()
        else -> fail(message.replace("{actual}", ch.toString()))
    }
}

val digit get() = char("Character {actual} is not a digit", Char::isDigit)
val letter get() = char("Character {actual} is not a letter", Char::isLetter)
val anyChar get() = char()
val number get() = some(digit).map { it.joinToString("").toInt() }
val longNumber get() = some(digit).map { it.joinToString("").toLong() }
val signedNumber = seq(optional(char('-')), number) { m, n -> if (m == "-") -n else n }

val Parser<Char, List<Char>>.asString get(): Parser<Char, String> = this.map {
    it.joinToString("")
}

