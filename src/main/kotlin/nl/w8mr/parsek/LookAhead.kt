package nl.w8mr.parsek

fun <R> lookAhead(parser: Parser<R>) = object: Parser<Unit>() {
    override fun apply(context: Context): Result<Unit> {
        val cur = context.index
        return when (val result = parser.apply(context)) {
            is Success -> {
                context.index = cur
                context.success(Unit, 0 )
            }
            is Error -> context.error("look ahead failed", subResults = listOf(result))
        }

    }
}