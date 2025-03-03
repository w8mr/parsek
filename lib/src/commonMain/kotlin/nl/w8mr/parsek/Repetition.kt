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

operator fun <Token, R> Parser<Token, R>.times(times: Int) = repeat(this, times, times)
operator fun <Token, R> Int.times(parser: Parser<Token, R>) = repeat(parser, this, this)
operator fun <Token, R> IntRange.times(parser: Parser<Token, R>) = repeat(parser, this.last, this.first)

fun <Token, R> oneOrMore(parser: Parser<Token, R>): Parser<Token, List<R>> = repeat(parser, min = 1)
fun <Token, R> some(parser: Parser<Token, R>): Parser<Token, List<R>> = repeat(parser, min = 1)

fun <Token, R> zeroOrMore(parser: Parser<Token, R>): Parser<Token, List<R>> = repeat(parser)
fun <Token, R> any(parser: Parser<Token, R>): Parser<Token, List<R>> = repeat(parser)

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
        val result = stop.bindAsResult()
        if (result is Parser.Success) {
            return@combi list.map { it.value } to result.value
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

fun <Token, R, S> sepByGreedy(
    parser: Parser<Token, R>,
    sep: Parser<Token, S>
) = combi {
    val start = -repeat(parser, 0, 1)
    val others = -zeroOrMore(seq(sep, parser) { _, p -> p })
    start + others
}