package nl.w8mr.parsek

fun <R1, R2, R> seq(p1: Parser<R1>, p2: Parser<R2>, map: (v1: R1, v2: R2) -> R) = object: Parser<R>() {
    override fun apply(context: Context): Result<R> =
        when (val r1 = p1.apply(context)) {
            is Success ->
                when (val r2 = p2.apply(context)) {
                    is Success -> context.success(map(r1.value, r2.value), 0)
                    is Error -> context.error("seq second parser failed", subResults = listOf(r1, r2))
                }
            is Error -> context.error("seq first parser failed", subResults = listOf(r1))
        }
}

fun <R1, R2> seq(p1: Parser<R1>, p2: Parser<R2>) =
    seq(p1,p2) { v1, v2 -> Pair(v1, v2) }

fun <R1, R2, R3, R> seq(p1: Parser<R1>, p2: Parser<R2>, p3: Parser<R3>, map: (v1: R1, v2: R2, v3: R3) -> R) =
    seq(seq(p1, p2), p3) { (v1, v2), v3 -> map(v1, v2, v3) }

fun <R1, R2, R3, R4, R> seq(p1: Parser<R1>, p2: Parser<R2>, p3: Parser<R3>, p4: Parser<R4>, map: (v1: R1, v2: R2, v3: R3, v4: R4) -> R) =
    seq(seq(p1, p2), seq(p3, p4)) { (v1, v2), (v3, v4) -> map(v1, v2, v3, v4) }

fun <R1, R2, R3, R4, R5, R> seq(p1: Parser<R1>, p2: Parser<R2>, p3: Parser<R3>, p4: Parser<R4>, p5: Parser<R5>, map: (v1: R1, v2: R2, v3: R3, v4: R4, v5: R5) -> R) =
    seq(seq(p1, p2), seq(p3, p4), p5) { (v1, v2), (v3, v4), v5 -> map(v1, v2, v3, v4, v5) }

fun <R1, R2, R3, R4, R5, R6, R> seq(p1: Parser<R1>, p2: Parser<R2>, p3: Parser<R3>, p4: Parser<R4>, p5: Parser<R5>, p6: Parser<R6>, map: (v1: R1, v2: R2, v3: R3, v4: R4, v5: R5, v6: R6) -> R) =
    seq(seq(p1, p2), seq(p3, p4), seq(p5, p6) { v5, v6 -> Pair(v5, v6) }) { (v1, v2), (v3, v4), (v5, v6) -> map(v1, v2, v3, v4, v5, v6) }

infix fun <R: Any?> Parser<Unit>.prefixLiteral(parser : Parser<R>): Parser<R> = seq(this, parser) { _, r -> r}
infix fun <R: Any?> Parser<R>.postfixLiteral(parser : Parser<Unit>): Parser<R> = seq(this, parser) { r, _ -> r}
