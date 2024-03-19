package nl.w8mr.parsek

infix fun <R> Parser<*>.failOr(parser: Parser<R>): Parser<R> = object: Parser<R>() {
    override fun apply(context: Context): Result<R> {
        val cur = context.index
        return when (this@failOr.apply(context)) {
            is Success -> {
                context.index = cur
                context.error("fail on successful first parser")
            }
            else -> parser.apply(context)
        }
    }
}
