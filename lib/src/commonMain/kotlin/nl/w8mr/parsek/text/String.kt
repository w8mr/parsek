package nl.w8mr.parsek.text

import nl.w8mr.parsek.Parser
import nl.w8mr.parsek.ParserSource

/**
 *  Matches on the [expected] string and failes with [message] if it succeeds the string is returned
 *
 * Example of parsing for a string
 * <!--- ZIPDOK include src/commonTest/nl/w8mr/parsek/text/StringTest#string-fixed -->
 * ```kotlin
 *    val parser = string("add")
 *    parser.parse("add 1") shouldBe "add"
 *  ```
 * <!--- ZIPDOK end -->
 */
fun string(expected: String, message: String = "Character {actual} does not meet expected {expected}, partial match: {partial}") = object : Parser<Char, String> {
    override fun applyImpl(source: ParserSource<Char>): Parser.Result<String> {
        var matched = mutableListOf<Char>()
        for (expectedChar in expected) {
            when (val char = source.next()) {
                expectedChar -> matched += char
                else -> when {
                    matched.isEmpty() -> failure(message.replace("{actual}", char.toString()).replace("{expected}", expected.toString()).replace("{partial}", "<None>"))
                    else -> failure(message.replace("{actual}", char.toString()).replace("{expected}", expected.toString()).replace("{partial}", matched.joinToString("")))
                }
            }
        }
        return success(matched.joinToString(""))
    }
}
