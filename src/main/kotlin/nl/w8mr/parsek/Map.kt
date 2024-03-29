package nl.w8mr.parsek

infix fun <R1, R> Parser<R1>.map(map: (value: R1) -> R) =
    object : Parser<R>() {
        override fun apply(context: Context): Result<R> =
            when (val result = this@map.apply(context)) {
                is Success -> context.success(map(result.value), 0)
                is Error -> context.error("Map failed", subResults = listOf(result))
            }
    }

infix fun <R1, R> Parser<R1>.mapResult(map: (value: Parser.Result<R1>) -> Parser.Result<R>) =
    object : Parser<R>() {
        override fun apply(context: Context): Result<R> = map(this@mapResult.apply(context))
    }

infix fun <R> Parser<R>.filter(predicate: (value: R) -> Boolean) =
    mapResult {
        when (it) {
            is Parser.Success ->
                if (predicate(it.value)) {
                    it
                } else {
                    Parser.Error("predicate not matched")
                }
            else -> it
        }
    }
