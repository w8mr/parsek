package nl.w8mr.parsek

fun <T, S, R1, R2, R> seq(p1: Parser<T, S, R1>, p2: Parser<T, S, R2>, map: (v1: R1, v2: R2) -> R) = object: Parser<T, S, R>() {
    override fun apply(context: Context<T, S>): Result<R> =
        when (val r1 = p1.apply(context)) {
            is Success ->
                when (val r2 = p2.apply(context)) {
                    is Success -> context.success(map(r1.value, r2.value), 0)
                    is Error -> context.error("seq second parser failed", subResults = listOf(r1, r2))
                }
            is Error -> context.error("seq first parser failed", subResults = listOf(r1))
        }
}

fun <T, S, R1, R2> seq(p1: Parser<T, S, R1>, p2: Parser<T, S, R2>) =
    seq(p1,p2) { v1, v2 -> Pair(v1, v2) }
