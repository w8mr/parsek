package nl.w8mr.parsek

fun <Token, R> oneOf(vararg parsers: Parser<Token, out R>) = combi("{error}") {
    for (parser in parsers) {
        val result = parser.bindAsResult()
        if (result is Parser.Success) {
            return@combi result.value
        }
    }
    fail("None of the parsers matches")
}

//fun <Token, R> oneOf(vararg parsers: Parser<Token, out R) = combi("{error}") {
//    parsers.asSequence().mapNotNull {
//        when(val result = it.bindResult()) {
//            is Parser.Success -> result.value
//            else -> null
//        }
//    }.firstOrNull() ?: fail("None of the parsers matches")
//}

infix fun <Token, R> Parser<Token, out R>.or(other: Parser<Token, out R>) = oneOf(this, other)
