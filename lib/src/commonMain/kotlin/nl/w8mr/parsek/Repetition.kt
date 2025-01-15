package nl.w8mr.parsek

fun <Token, R> repeat(
    parser: Parser<Token, R>,
    max: Int = Int.MAX_VALUE,
    min: Int = 0,
) = combi("{error}") {
        val list = mutableListOf<Parser.Success<R>>()
        while (list.size < max) {
            when (val result = parser.bindAsResult()) {
                is Parser.Success -> list.add(result)
                is Parser.Error -> break
            }
        }
        when {
            list.size < min -> Parser.Error("Repeat only ${list.size} elements found, needed at least $min")
            else -> Parser.Success(list.map { it.value })
        }.bind()
    }

operator fun <Token, R> Parser<Token, R>.times(times: Int) = repeat(this, times, times)
operator fun <Token, R> Int.times(parser: Parser<Token, R>) = repeat(parser, this, this)
operator fun <Token, R> IntRange.times(parser: Parser<Token, R>) = repeat(parser, this.last, this.first)

fun <Token, R> oneOrMore(parser: Parser<Token, R>): Parser<Token, List<R>> = repeat(parser, min = 1)
fun <Token, R> some(parser: Parser<Token, R>): Parser<Token, List<R>> = repeat(parser, min = 1)

fun <Token, R> zeroOrMore(parser: Parser<Token, R>): Parser<Token, List<R>> = repeat(parser)
