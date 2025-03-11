package nl.w8mr.parsek.text

import nl.w8mr.parsek.*

fun <Token> repeat(
    parser: Parser<Token, String>,
    max: Int = Int.MAX_VALUE,
    min: Int = 0,
) = object: Parser<Token, String> {
    override fun applyImpl(source: ParserSource<Token>) = when (val result = nl.w8mr.parsek.repeat(parser, max, min).applyImpl(source)) {
            is Parser.Success -> success(result.value.joinToString(""), result.subResults)
            is Parser.Failure -> failure(result.message, result.subResults)
        }
    }

fun <S> untilLazy(
    repeat: Parser<Char, String>,
    stop: Parser<Char, S>,
    max: Int = Int.MAX_VALUE,
    min: Int = 0,
) = object: Parser<Char, Pair<String, S>> {
    override fun applyImpl(source: ParserSource<Char>) = when (val result = nl.w8mr.parsek.untilLazy(repeat, stop, max, min).applyImpl(source)) {
        is Parser.Success -> success(result.value.first.joinToString("") to result.value.second, result.subResults)
        is Parser.Failure -> failure(result.message, result.subResults)
    }
}

fun untilLazy(
    repeat: Parser<Char, String>,
    stop: LiteralParser<Char>,
    max: Int = Int.MAX_VALUE,
    min: Int = 0,
) = untilLazy(repeat, stop as Parser<Char, Unit>, max, min).map { it.first }

infix fun Parser<Char, String>.until(stop: Char) = untilLazy(this, literal(stop))

fun optional(char: Char) = optional(literal(char))
fun optional(text: String) = optional(literal(text))

operator fun <Token> Parser<Token, String>.times(times: Int) = repeat(this, times, times)
operator fun <Token> Int.times(parser: Parser<Token, String>) = repeat(parser, this, this)
operator fun <Token> IntRange.times(parser: Parser<Token, String>) = repeat(parser, this.last, this.first)

fun <Token> oneOrMore(parser: Parser<Token, String>): Parser<Token, String> = repeat(parser, min = 1)
fun <Token> some(parser: Parser<Token, String>): Parser<Token, String> = repeat(parser, min = 1)

fun <Token> zeroOrMore(parser: Parser<Token, String>): Parser<Token, String> = repeat(parser)
fun <Token> any(parser: Parser<Token, String>): Parser<Token, String> = repeat(parser)

infix fun <R> Parser<Char, R>.sepBy(other: Char) = sepByGreedy(this, literal(other))
infix fun <R> Parser<Char, R>.sepBy(other: String) = sepByGreedy(this, literal(other))