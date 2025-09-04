package nl.w8mr.parsek

fun <Token, R> oneOf(vararg parsers: Parser<Token, out R>) = combi<Token, R>("{error}") {
    for (parser in parsers) {
        val result = parser.bindAsResult()
        if (result is Parser.Success) {
            return@combi result.value
        }
    }
    fail("None of the parsers matches")
}

fun <Token> oneOf(vararg parsers: LiteralParser<Token>) = literalCombi<Token>("{error}") {
    for (parser in parsers) {
        val result = parser.bindAsResult()
        if (result is Parser.Success) {
            return@literalCombi
        }
    }
    fail("None of the parsers matches")
}

infix fun <Token, R> Parser<Token, out R>.or(other: Parser<Token, out R>) = oneOf(this, other)
infix fun <Token> LiteralParser<Token>.or(other: LiteralParser<Token>) = oneOf(this, other)
