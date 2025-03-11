package nl.w8mr.parsek.text

import nl.w8mr.parsek.LiteralParser
import nl.w8mr.parsek.Parser
import nl.w8mr.parsek.ParserSource
import nl.w8mr.parsek.value

fun literal(expected: Char, message: String = "Character {actual} does not meet expected {expected}") = object :
    LiteralParser<Char> {
    override fun applyImpl(source: ParserSource<Char>): Parser.Result<Unit> = when (val char = source.next()) {
        expected -> success(Unit)
        else -> failure(message.replace("{actual}", char.toString()).replace("{expected}", expected.toString()))
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
fun literal(expected: String, message: String = "Character {actual} does not meet expected {expected}, partial match: {partial}") = object : LiteralParser<Char> {
    override fun applyImpl(source: ParserSource<Char>): Parser.Result<Unit> {
        var matched = mutableListOf<Char>()
        val mark = source.mark()
        for (expectedChar in expected) {
            when (val char = source.next()) {
                expectedChar -> matched += char
                else -> {
                    source.reset(mark)
                    return when {
                        matched.isEmpty() -> failure(message.replace("{actual}", char.toString()).replace("{expected}", expectedChar.toString()).replace("{partial}", "<None>"))
                        else -> failure(message.replace("{actual}", char.toString()).replace("{expected}", expectedChar.toString()).replace("{partial}", matched.joinToString("")))
                    }
                }
            }
        }
        source.release(mark)
        return success(Unit)
    }
}

infix fun <R> String.value(value: R) = literal(this).value(value)
infix fun <R> Char.value(value: R) = literal(this).value(value)
