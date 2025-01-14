package nl.w8mr.parsek

import nl.w8mr.parsek.Parser
import nl.w8mr.parsek.combi

fun <R, Token> repeat(
    parser: Parser<R, Token>,
    max: Int = Int.MAX_VALUE,
    min: Int = 0,
) = combi("{error}") {
        tryParser {
            val list = mutableListOf<Parser.Success<R>>()
            while (list.size < max) {
                when (val result = tryParser(parser)) {
                    is Parser.Success -> list.add(result)
                    is Parser.Error -> break
                }
            }
            when {
                list.size < min -> Parser.Error("Repeat only ${list.size} elements found, needed at least $min")
                else -> Parser.Success(list.map { it.value })
            }
        }.bind()
    }

operator fun <R, Token> Parser<R, Token>.times(times: Int) = repeat(this, times, times)
operator fun <R, Token> Int.times(parser: Parser<R, Token>) = repeat(parser, this, this)
operator fun <R, Token> IntRange.times(parser: Parser<R, Token>) = repeat(parser, this.last, this.first)

fun <R, Token> oneOrMore(parser: Parser<R, Token>): Parser<List<R>, Token> = repeat(parser, min = 1)
fun <R, Token> some(parser: Parser<R, Token>): Parser<List<R>, Token> = repeat(parser, min = 1)

fun <R, Token> zeroOrMore(parser: Parser<R, Token>): Parser<List<R>, Token> = repeat(parser)
