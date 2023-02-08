package nl.w8mr.parsek

operator fun <R> Parser<R>.times(times: Int) = repeat(this, times, times)

operator fun <R> Int.times(parser: Parser<R>) = repeat(parser, this, this)

operator fun <R> IntRange.times(parser: Parser<R>) = repeat(parser, this.start, this.endInclusive)

fun <R> oneOrMore(parser: Parser<R>): Parser<List<R>> = repeat(parser, min = 1)

fun <R> zeroOrMore(parser: Parser<R>): Parser<List<R>> = repeat(parser)


infix fun <R> Parser<R>.sepBy(separator: Parser<*>) = object: Parser<List<R>>() {
    override fun apply(context: Context): Result<List<R>> {
        val list = mutableListOf<R>()
        while (context.hasNext()) {
            val cur = context.index
            when (val result = this@sepBy.apply(context)) {
                is Success -> list.add(result.value)
                else -> {
                    context.index = cur
                    break
                }
            }
            val cur2 = context.index
            when (separator.apply(context)) {
                is Success -> {}
                else -> {
                    context.index = cur2
                    break
                }
            }
        }
        return context.success(list, 0)
    }
}

fun <R> repeat(parser: Parser<R>, max: Int = Int.MAX_VALUE, min: Int = 0) = object: Parser<List<R>>() {
    override fun apply(context: Context): Result<List<R>> {
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
