package nl.w8mr.parsek.text

import nl.w8mr.parsek.Parser
import nl.w8mr.parsek.ParserSource
import nl.w8mr.parsek.combi
import nl.w8mr.parsek.sepByGreedy

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

fun <Token, S> untilLazy(
    repeat: Parser<Token, String>,
    stop: Parser<Token, S>,
    max: Int = Int.MAX_VALUE,
    min: Int = 0,
) = object: Parser<Token, Pair<String, S>> {
    override fun applyImpl(source: ParserSource<Token>) = when (val result = nl.w8mr.parsek.untilLazy(repeat, stop, max, min).applyImpl(source)) {
        is Parser.Success -> success(result.value.first.joinToString("") to result.value.second, result.subResults)
        is Parser.Failure -> failure(result.message, result.subResults)
    }
}


operator fun <Token> Parser<Token, String>.times(times: Int) = repeat(this, times, times)
operator fun <Token> Int.times(parser: Parser<Token, String>) = repeat(parser, this, this)
operator fun <Token> IntRange.times(parser: Parser<Token, String>) = repeat(parser, this.last, this.first)

fun <Token> oneOrMore(parser: Parser<Token, String>): Parser<Token, String> = repeat(parser, min = 1)
fun <Token> some(parser: Parser<Token, String>): Parser<Token, String> = repeat(parser, min = 1)

fun <Token> zeroOrMore(parser: Parser<Token, String>): Parser<Token, String> = repeat(parser)
fun <Token> any(parser: Parser<Token, String>): Parser<Token, String> = repeat(parser)

infix fun <R> Parser<Char, R>.sepBy(other: Char) = sepByGreedy(this, literal(other))