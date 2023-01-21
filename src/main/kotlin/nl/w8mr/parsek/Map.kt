package nl.w8mr.parsek

infix fun <T, S, R1, R> Parser<T, S, R1>.map(map: (value: R1) -> R)  = object : Parser<T, S, R>() {
    override fun apply(context: Context<T, S>): Result<R> =
        when (val result = this@map.apply(context)) {
            is Success -> context.success(map(result.value), 0)
            is Error -> context.error("Map failed", subResults = listOf(result))
        }
    }
