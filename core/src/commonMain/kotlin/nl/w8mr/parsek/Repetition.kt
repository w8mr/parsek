package nl.w8mr.parsek

fun <Token, R> repeat(
    parser: Parser<Token, R>,
    max: Int = Int.MAX_VALUE,
    min: Int = 0,
): Parser<Token, List<R>> = combi("{error}") {
        val list = mutableListOf<Parser.Success<R>>()
        while (list.size < min) {
            when (val result = parser.bindAsResult()) {
                is Parser.Success -> list.add(result)
                is Parser.Failure -> fail("Repeat only ${list.size} elements found, needed at least $min")
            }
        }
        while (list.size < max) {
            when (val result = parser.bindAsResult()) {
                is Parser.Success -> list.add(result)
                is Parser.Failure -> break
            }
        }
        list.map { it.value }
    }

//infix fun <R, S, Token> Parser<Token, R>.sepByAllowEmpty(other: Parser<Token, S>) = combi<Token, List<R>> {
//    val p: Parser<Token, R> = this@sepByAllowEmpty
//    when (val firstResult = p.bindAsResult() ) {
//        is Parser.Failure<R> -> { emptyList<R>() }
//        is Parser.Success<R> -> {
//            val result = mutableListOf<R>(firstResult.value)
//            while(true) {
//                val mark = mark()
//                when (val nextResult = (other.asLiteral() and p).bindAsResult()) {
//                    is Parser.Failure<R> -> {
//                        reset(mark)
//                        break
//                    }
//                    is Parser.Success<R> -> {
//                        result.add(nextResult.value)
//                    }
//                }
//            }
//            result.toList()
//        }
//    }
//
//}
//
//
//

fun <Token> repeat(parser: LiteralParser<Token>, max: Int = Int.MAX_VALUE, min: Int = 0): LiteralParser<Token> = repeat(parser as Parser<Token, Unit>, max, min).asLiteral()

fun <Token, R: Any> optional(parser: Parser<Token, R>): Parser<Token, R?> = repeat(parser, 1, 0).map { if (it.isNotEmpty()) it[0] else null }
fun <Token> optional(parser: LiteralParser<Token>) = repeat(parser, 1, 0)


operator fun <Token, R> Parser<Token, R>.times(times: Int) = repeat(this, times, times)
operator fun <Token, R> Int.times(parser: Parser<Token, R>) = repeat(parser, this, this)
operator fun <Token, R> IntRange.times(parser: Parser<Token, R>) = repeat(parser, this.last, this.first)

fun <Token, R> oneOrMore(parser: Parser<Token, R>): Parser<Token, List<R>> = repeat(parser, min = 1)
fun <Token, R> some(parser: Parser<Token, R>): Parser<Token, List<R>> = repeat(parser, min = 1)

fun <Token, R> zeroOrMore(parser: Parser<Token, R>): Parser<Token, List<R>> = repeat(parser)
fun <Token, R> any(parser: Parser<Token, R>): Parser<Token, List<R>> = repeat(parser)
fun <Token> any(parser: LiteralParser<Token>): LiteralParser<Token> = repeat(parser).asLiteral()

fun <Token, R, S> untilLazy(
    repeat: Parser<Token, R>,
    stop: Parser<Token, S>,
    max: Int = Int.MAX_VALUE,
    min: Int = 0,
): Parser<Token, Pair<List<R>, S>> = combi("{error}") {
    val list = mutableListOf<Parser.Success<R>>()
    while (list.size < min) {
        when (val result = repeat.bindAsResult()) {
            is Parser.Success -> list.add(result)
            is Parser.Failure -> fail("Repeat only ${list.size} elements found, needed at least $min")
        }
    }
    while (list.size < max) {
        val resultStop = stop.bindAsResult()
        if (resultStop is Parser.Success) {
            return@combi list.map { it.value } to resultStop.value
        }
        when (val result = repeat.bindAsResult()) {
            is Parser.Success -> list.add(result)
            is Parser.Failure -> break
        }
    }
    val result = stop.bindAsResult()
    if (result is Parser.Success) {
        list.map { it.value } to result.value
    } else {
        fail("Stop not found")
    }
}

fun <Token, R> untilLazy(
    repeat: Parser<Token, R>,
    stop: LiteralParser<Token>,
    max: Int = Int.MAX_VALUE,
    min: Int = 0,
) = untilLazy(repeat, stop as Parser<Token, Unit>, max, min).map { (r, _) -> r }

fun <Token, R, S> sepByGreedy(
    parser: Parser<Token, R>,
    sep: Parser<Token, S>
) = combi {
    val start = parser.bind()
    val others = zeroOrMore(seq(sep, parser) { _, p -> p }).bind()
    listOf(start) + others
}

fun <Token, R, S> sepByGreedyAllowEmpty(
    parser: Parser<Token, R>,
    sep: Parser<Token, S>
) = combi {
    val start = when (val first = parser.bindAsResult()) {
        is Parser.Success -> first.value
        is Parser.Failure -> return@combi emptyList()
    }
    val others = -zeroOrMore(seq(sep, parser) { _, p -> p })
    listOf(start) + others
}


infix fun <Token, R, S> Parser<Token, R>.sepBy(other: Parser<Token, S>) = sepByGreedy(this, other)
infix fun <Token, R, S, > Parser<Token, R>.sepByAllowEmpty(other: Parser<Token, S>) = sepByGreedyAllowEmpty(this, other)
