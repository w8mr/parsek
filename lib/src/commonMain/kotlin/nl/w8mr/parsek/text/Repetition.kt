package nl.w8mr.parsek.text

import nl.w8mr.parsek.Parser
import nl.w8mr.parsek.ParserSource
import nl.w8mr.parsek.combi

fun <Token> repeat(
    parser: TextParser<Token>,
    max: Int = Int.MAX_VALUE,
    min: Int = 0,
) = object: TextParser<Token> {
    override fun applyImpl(source: ParserSource<Token>) = when (val result = nl.w8mr.parsek.repeat(parser, max, min).applyImpl(source)) {
            is Parser.Success -> success(result.value.joinToString(""), result.subResults)
            is Parser.Failure -> failure(result.message, result.subResults)
        }
    }

fun <Token, S> untilLazy(
    repeat: TextParser<Token>,
    stop: Parser<Token, S>,
    max: Int = Int.MAX_VALUE,
    min: Int = 0,
) = object: Parser<Token, Pair<String, S>> {
    override fun applyImpl(source: ParserSource<Token>) = when (val result = nl.w8mr.parsek.untilLazy(repeat, stop, max, min).applyImpl(source)) {
        is Parser.Success -> success(result.value.first.joinToString("") to result.value.second, result.subResults)
        is Parser.Failure -> failure(result.message, result.subResults)
    }
}


operator fun <Token> TextParser<Token>.times(times: Int) = repeat(this, times, times)
operator fun <Token> Int.times(parser: TextParser<Token>) = repeat(parser, this, this)
operator fun <Token> IntRange.times(parser: TextParser<Token>) = repeat(parser, this.last, this.first)

fun <Token> oneOrMore(parser: TextParser<Token>): TextParser<Token> = repeat(parser, min = 1)
fun <Token> some(parser: TextParser<Token>): TextParser<Token> = repeat(parser, min = 1)

fun <Token> zeroOrMore(parser: TextParser<Token>): TextParser<Token> = repeat(parser)
