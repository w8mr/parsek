package nl.w8mr.parsek.text

import nl.w8mr.parsek.ParserSource
import nl.w8mr.parsek.Parser
import nl.w8mr.parsek.map
import nl.w8mr.parsek.optional
import nl.w8mr.parsek.seq
import nl.w8mr.parsek.some

/**
 *  Matches on the [expected] character and failes with [message] if it succeeds the character is returned as
 *  a string for easier combining with other porsers
 *
 * Example of parsing for a single character
 * <!--- ZIPDOK include src/commonTest/nl/w8mr/parsek/text/CharTest#char-literal -->
 * ```kotlin
 *    val parser = char('a')
 *    parser.parse("ab") shouldBe "a"
 *  ```
 * <!--- ZIPDOK end -->
 */
fun char(expected: Char, message: String = "Character {actual} does not meet expected {expected}") = object : Parser<Char, String> {
    override fun applyImpl(source: ParserSource<Char>): Parser.Result<String> = when (val char = source.next()) {
        expected -> success(expected.toString())
        else -> failure(message.replace("{actual}", char.toString()).replace("{expected}", expected.toString()))
    }
}

fun char(message: String = "{actual} found, not a regular character") = object : Parser<Char, String> {
    override fun applyImpl(source: ParserSource<Char>): Parser.Result<String> =
        source.next()?.let { success(it.toString()) } ?: failure(message.replace("{actual}", "{EoF}"))
}

fun char(message: String = "Character {actual} does not meet predicate", predicate: (Char) -> Boolean) = object : Parser<Char, String> {
    override fun applyImpl(source: ParserSource<Char>): Parser.Result<String> =
        source.next()?.let { char ->
            if (predicate(char)) success(char.toString())
            else failure(message.replace("{actual}", char.toString()))
        } ?: failure(message.replace("{actual}", "{EoF}"))

}

val digit get() = char("Character {actual} is not a digit", Char::isDigit)
val letter get() = char("Character {actual} is not a letter", Char::isLetter)
val char get() = char()
val number get() = some(digit).map { it.joinToString("").toInt() }
val longNumber get() = some(digit).map { it.joinToString("").toLong() }
val signedNumber = seq(optional(char('-')), number) { m, n -> if (m == "-") -n else n }

val Parser<Char, List<Char>>.asString get(): Parser<Char, String> = this.map {
    it.joinToString("")
}

