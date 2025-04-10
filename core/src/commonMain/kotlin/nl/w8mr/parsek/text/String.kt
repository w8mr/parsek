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
        return success(matched.joinToString(""))
    }
}

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
