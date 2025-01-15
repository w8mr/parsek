package nl.w8mr.parsek.text

import nl.w8mr.parsek.ParserSource
import nl.w8mr.parsek.Parser
import nl.w8mr.parsek.map
import nl.w8mr.parsek.some

/**
 *  Matches on the [expected] character and failes with [message]
 *
 * Example of parsing for a single character
 * <!--- ZIPDOK include src/commonTest/nl/w8mr/parsek/text/CharTest#char-literal -->
 * ```kotlin
 *     val parser = char('a')
 *     parser.parse("ab") shouldBe 'a'
 * ```
 * <!--- ZIPDOK end -->
 */
fun char(expected: Char, message: String = "Character {actual} does not meet expected {expected}") = object : TextParser<Char> {
    override fun applyImpl(source: ParserSource<Char>): Parser.Result<Char> = when (val char = source.next()) {
        expected -> Parser.Success(expected)
        else -> Parser.Error(message.replace("{actual}", char.toString()).replace("{expected}", expected.toString()))
    }
}

fun char(message: String = "{actual} found, not a regular character") = object : TextParser<Char> {
    override fun applyImpl(source: ParserSource<Char>): Parser.Result<Char> =
        source.next()?.let { Parser.Success(it) } ?: Parser.Error<Char>(message.replace("{actual}", "{EoF}"))
}

fun char(message: String = "Character {actual} does not meet predicate", predicate: (Char) -> Boolean) = object : TextParser<Char> {
    override fun applyImpl(source: ParserSource<Char>): Parser.Result<Char> =
        source.next()?.let { char ->
            if (predicate(char)) Parser.Success(char)
            else Parser.Error(message.replace("{actual}", char.toString()))
        } ?: Parser.Error<Char>(message.replace("{actual}", "{EoF}"))

}



val digit get() = char("Character {actual} is not a digit", Char::isDigit)
val letter get() = char("Character {actual} is not a letter", Char::isLetter)
val char get() = char()
val number get() = some(digit).map { it.joinToString("").toInt() }

val Parser<Char, List<Char>, >.asString get(): Parser<Char, String> = this.map {
    it.joinToString("")
}

