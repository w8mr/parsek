package nl.w8mr.parsek.text

import nl.w8mr.parsek.Context
import nl.w8mr.parsek.Parser

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
fun  string(expected: String, message: String = "Character {actual} does not meet expected {expected}, partial match: {partial}") = object : Parser<Char, String> {
    override fun apply(context: Context<Char>): Pair<Parser.Result<String>, Context<Char>> {
        var matched = mutableListOf<Char>()
        var current = context
        for (expectedChar in expected) {
            when (current.hasNext()) {
                true -> {
                    val (char, new) = current.token()
                    when (char) {
                        expectedChar -> {
                            current = new
                            matched += char
                        }
                        else -> {
                            return when {
                                matched.isEmpty() -> failure(
                                    message.replace("{actual}", char.toString())
                                        .replace("{expected}", expectedChar.toString()).replace("{partial}", "<None>")
                                ) to context

                                else -> failure(
                                    message.replace("{actual}", char.toString())
                                        .replace("{expected}", expectedChar.toString())
                                        .replace("{partial}", matched.joinToString(""))
                                ) to context
                            }
                        }
                    }
                }
                false -> failure(message.replace("{actual}", "{EoF}")) to context
            }
        }
        return success(matched.joinToString("")) to current
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
