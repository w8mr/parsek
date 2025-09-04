package nl.w8mr.parsek.text

import nl.w8mr.parsek.*

fun <Token> repeat(
    parser: Parser<Token, String>,
    max: Int = Int.MAX_VALUE,
    min: Int = 0,
) = direct <Token, String> {
    nl.w8mr.parsek.repeat(parser, max, min).bindAsResult().map { it.joinToString("") }
}

fun <S> untilLazy(
    repeat: Parser<Char, String>,
    stop: Parser<Char, S>,
    max: Int = Int.MAX_VALUE,
    min: Int = 0,
) = direct {
        nl.w8mr.parsek.untilLazy(repeat, stop, max, min).bindAsResult().map { it.first.joinToString("") to it.second }
}

fun untilLazy(
    repeat: Parser<Char, String>,
    stop: LiteralParser<Char>,
    max: Int = Int.MAX_VALUE,
    min: Int = 0,
) = untilLazy(repeat, stop as Parser<Char, Unit>, max, min).map { (s, u) -> s }

infix fun Parser<Char, String>.until(stop: Char) = untilLazy(this, literal(stop))

fun optional(char: Char) = optional(literal(char))
fun optional(text: String) = optional(literal(text))

operator fun <Token> Parser<Token, String>.times(times: Int) = repeat(this, times, times)
operator fun <Token> Int.times(parser: Parser<Token, String>) = repeat(parser, this, this)
operator fun <Token> IntRange.times(parser: Parser<Token, String>) = repeat(parser, this.last, this.first)

fun <Token> oneOrMore(parser: LiteralParser<Token>): LiteralParser<Token> = repeat(parser, min = 1)
fun <Token> some(parser: LiteralParser<Token>): LiteralParser<Token> = repeat(parser, min = 1)

fun <Token> oneOrMore(parser: Parser<Token, String>): Parser<Token, String> = repeat(parser, min = 1)
fun <Token> some(parser: Parser<Token, String>): Parser<Token, String> = repeat(parser, min = 1)

fun <Token> zeroOrMore(parser: LiteralParser<Token>): LiteralParser<Token> = repeat(parser)
fun <Token> any(parser: LiteralParser<Token>): LiteralParser<Token> = repeat(parser)

fun <Token> zeroOrMore(parser: Parser<Token, String>): Parser<Token, String> = repeat(parser)
fun <Token> any(parser: Parser<Token, String>): Parser<Token, String> = repeat(parser)

infix fun <R> Parser<Char, R>.sepBy(other: Char) = sepByGreedy(this, literal(other))
infix fun <R> Parser<Char, R>.sepBy(other: String) = sepByGreedy(this, literal(other))