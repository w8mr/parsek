package nl.w8mr.parsek.text

import nl.w8mr.parsek.Parser

fun <Token> repeat(
    parser: TextParser<Token>,
    max: Int = Int.MAX_VALUE,
    min: Int = 0,
): TextParser<Token> = textCombi<Token>("{error}") {
        val list = mutableListOf<Parser.Success<String>>()
        while (list.size < max) {
            when (val result = parser.bindAsResult()) {
                is Parser.Success -> list.add(result)
                is Parser.Failure -> break
            }
        }
        when {
            list.size < min -> failure("Repeat only ${list.size} elements found, needed at least $min")
            else -> success(list.map(Parser.Success<String>::value).joinToString(""))
        }.bind()
    }

operator fun <Token> TextParser<Token>.times(times: Int) = repeat(this, times, times)
operator fun <Token> Int.times(parser: TextParser<Token>) = repeat(parser, this, this)
operator fun <Token> IntRange.times(parser: TextParser<Token>) = repeat(parser, this.last, this.first)

fun <Token> oneOrMore(parser: TextParser<Token>): TextParser<Token> = repeat(parser, min = 1)
fun <Token> some(parser: TextParser<Token>): TextParser<Token> = repeat(parser, min = 1)

fun <Token> zeroOrMore(parser: TextParser<Token>): TextParser<Token> = repeat(parser)
