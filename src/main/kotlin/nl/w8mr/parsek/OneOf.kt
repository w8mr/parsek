package nl.w8mr.parsek

fun <R> oneOf(vararg parsers: Parser<out R>) =
    object : Parser<R>() {
        override fun apply(context: Context): Result<R> {
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

fun <R> longestMatch(vararg parsers: Parser<out R>) =
    object : Parser<R>() {
        override fun apply(context: Context): Result<R> {
            val errors = mutableListOf<Error<*>>()
            var success = mutableListOf<Pair<Int, Result<R>>>()
            for (parser in parsers) {
                val cur = context.index
                when (val result = parser.apply(context)) {
                    is Success -> success.add(context.index to context.success(result.value, 0))
                    is Error -> {
                        errors.add(result)
                        context.index = cur
                    }
                }
            }
            return when {
                success.isNotEmpty() -> success.maxBy { it.first }.second
                else -> context.error("oneOf has no match", subResults = errors)
            }
        }
    }

infix fun <R> Parser<out R>.or(other: Parser<out R>): Parser<R> = oneOf(this, other)

fun <R> optional(p: Parser<R>): Parser<R?> =
    p or_ empty() map { o ->
        when (o) {
            is Either.Left -> o.value
            is Either.Right -> null
        }
    }

fun empty() =
    object : Parser<Unit>() {
        override fun apply(context: Context): Result<Unit> = context.success(Unit, 0)
    }

sealed interface Either<out L, out R> {
    data class Left<L>(val value: L) : Either<L, Nothing>

    data class Right<R>(val value: R) : Either<Nothing, R>
}

infix fun <L, R> Parser<L>.or_(p2: Parser<R>) =
    object : Parser<Either<L, R>>() {
        override fun apply(context: Context): Result<Either<L, R>> =
            when (val r1 = this@or_.apply(context)) {
                is Success -> context.success(Either.Left(r1.value), 0)
                is Error ->
                    when (val r2 = p2.apply(context)) {
                        is Success -> context.success(Either.Right(r2.value), 0)
                        is Error -> context.error("aoc.Or both failed")
                    }
            }
    }
