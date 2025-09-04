package nl.w8mr.parsek.text

import nl.w8mr.parsek.Context
import nl.w8mr.parsek.LiteralParser
import nl.w8mr.parsek.Parser
import nl.w8mr.parsek.value

fun literal(expected: Char, message: String = "Character {actual} does not meet expected {expected}") = object :
    LiteralParser<Char> {
    override fun apply(context: Context<Char>): Pair<Parser.Result<Unit>, Context<Char>> {
        return when (context.hasNext()) {
            true -> {
                val (char, new) = context.token()
                when (char) {
                    expected -> success(Unit) to new
                    else -> failure(message.replace("{actual}", char.toString()).replace("{expected}", expected.toString())) to context
                }
            }
            false -> {
                failure(
                    message.replace("{actual}", "{EoF}").replace("{expected}", expected.toString())) to context
            }
        }


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
    override fun apply(context: Context<Char>): Pair<Parser.Result<Unit>, Context<Char>> {
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
        return success(Unit) to current
    }
}

infix fun <R> String.value(value: R) = literal(this).value(value)
infix fun <R> Char.value(value: R) = literal(this).value(value)
