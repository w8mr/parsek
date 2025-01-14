package nl.w8mr.parsek

fun <R, Token> oneOf(vararg parsers: Parser<out R, Token>) = combi("{error}") {
    for (parser in parsers) {
        val result = parser.bindResult()
        if (result is Parser.Success) {
            return@combi result.value
        }
    }
    fail("None of the parsers matches")
}

//fun <R, Token> oneOf(vararg parsers: Parser<out R, Token>) = combi("{error}") {
//    parsers.asSequence().mapNotNull {
//        when(val result = it.bindResult()) {
//            is Parser.Success -> result.value
//            else -> null
//        }
//    }.firstOrNull() ?: fail("None of the parsers matches")
//}
