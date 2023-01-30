package nl.w8mr.parsek

operator fun <T, S, R> Parser<T, S, R>.times(times: Int) = repeat(this, times, times)

operator fun <T, S, R> Int.times(parser: Parser<T, S, R>) = repeat(parser, this, this)

operator fun <T, S, R> IntRange.times(parser: Parser<T, S, R>) = repeat(parser, this.start, this.endInclusive)

fun <T, S, R> oneOrMore(parser: Parser<T, S, R>): Parser<T, S, List<R>> = repeat(parser, min = 1)

fun <T, S, R> zeroOrMore(parser: Parser<T, S, R>): Parser<T, S, List<R>> = repeat(parser)


infix fun <T, S, R> Parser<T, S, R>.sepBy(separator: Parser<T, S, *>) = zeroOrMore(seq(this, optional(separator)) { result, _ -> result})


fun <T, S, R> repeat(parser: Parser<T, S, R>, max: Int = Int.MAX_VALUE, min: Int = 0) = object: Parser<T, S, List<R>>() {
    override fun apply(context: Context<T, S>): Result<List<R>> {
        val list = mutableListOf<R>()
        val begin = context.index
        while (context.hasNext()) {
            val cur = context.index
            when (val result = parser.apply(context)) {
                is Success -> list.add(result.value)
                else -> {
                    context.index = cur
                    break
                }
            }
        }
        val size = list.size
        return when {
            size < min -> {
                context.index = begin
                context.error("repeat only $size elements found, needed at least $min")
            }

            size > max -> {
                context.index = begin
                context.error("repeat more elements found, needed at most $max")
            }

            else -> context.success(list, 0)
        }
    }
}
