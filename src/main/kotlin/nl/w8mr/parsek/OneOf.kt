package nl.w8mr.parsek

fun <T, S, R> oneOf(vararg parsers: Parser<T, S, out R>) = object: Parser<T, S, R>() {
    override fun apply(context: Context<T, S>): Result<R> {
        val errors = mutableListOf<Error<*>>()
        for (parser in parsers) {
            val cur = context.index
            when (val result = parser.apply(context)) {
                is Success -> return context.success(result.value, 0)
                is Error -> {
                    errors.add(result)
                    context.index = cur
                }
            }
        }
        return context.error("oneOf has no match", subResults = errors)
    }
}

infix fun <T, S, R> Parser<T, S, out R>.or(other: Parser<T, S, out R>): Parser<T, S, R> = oneOf(this, other)



fun <T, S, R> optional(p: Parser<T, S, R>) : Parser<T, S, R?> =
    p or_ empty() map { o ->
        when (o) {
            is Either.Left -> o.value
            is Either.Right -> null
        }
    }

fun <T, S> empty() = object: Parser<T, S, Unit>() {
    override fun apply(context: Context<T, S>): Result<Unit> =
        context.success(Unit, 0)
}

sealed interface Either<out L, out R> {
    data class Left<L>(val value: L) : Either<L, Nothing>
    data class Right<R>(val value: R) : Either<Nothing, R>
}

infix fun <T, S, L, R> Parser<T, S, L>.or_(p2: Parser<T, S, R>) =
    object: Parser<T, S, Either<L, R>>() {
        override fun apply(context: Context<T, S>): Result<Either<L, R>> =
            when (val r1 = this@or_.apply(context)) {
                is Success -> context.success(Either.Left(r1.value), 0)
                is Error ->
                    when (val r2 = p2.apply(context)) {
                        is Success -> context.success(Either.Right(r2.value), 0)
                        is Error -> context.error("aoc.Or both failed")
                    }
            }
    }

