package nl.w8mr.parsek.text

import nl.w8mr.parsek.Context
import nl.w8mr.parsek.Parser
import nl.w8mr.parsek.simple
import nl.w8mr.parsek.simpleLiteral

/**
 *  Matches on the [expected] string and failes with [message] if it succeeds the string is returned
 *
 * Example of parsing for a string
 * <!--- ZIPDOK include src/commonTest/nl/w8mr/parsek/text/StringTest#string-fixed -->
 * ```kotlin
 *    val parser = string("add")
 *    parser("add 1") shouldBe "add"
 *  ```
 * <!--- ZIPDOK end -->
 */
fun string(expected: String, message: String = "Character {actual} does not meet expected {expected}, partial match: {partial}") = simple<Char, String> {
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
    matched.joinToString("")
}

/**
 *  Matches on the [expected] string and failes with [message] if it succeeds the string is returned
 *
 * Example of parsing for a string
 * <!--- ZIPDOK include src/commonTest/nl/w8mr/parsek/text/StringTest#string-fixed -->
 * ```kotlin
 *    val parser = string("add")
 *    parser("add 1") shouldBe "add"
 *  ```
 * <!--- ZIPDOK end -->
 */
/*
fun string(predicate: (char: Char) -> Boolean, message: String = "Character {actual} does not meet predicate, partial match: {partial}") = object : Parser<Char, String> {
    override fun applyImpl(source: ParserSource<Char>): Parser.Result<String> {
        var matched = mutableListOf<Char>()
        val mark = source.mark()
        var char = source.next()
        while (char != null) {
            when {
                predicate(char) -> matched += char
                else -> {
                    source.reset(mark)
                    return when {
                        matched.isEmpty() -> failure(message.replace("{actual}", char.toString()).replace("{partial}", "<None>"))
                        else -> failure(message.replace("{actual}", char.toString()).replace("{partial}", matched.joinToString("")))
                    }
                }
            }
        }
        source.release(mark)
        return success(matched.joinToString(""))
    }
}
*/
