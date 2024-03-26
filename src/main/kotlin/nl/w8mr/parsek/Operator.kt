package nl.w8mr.parsek

fun <O, R> Parser<O>.binary(map : (O) -> (R, R) -> R) = object: Parser<(R,R) -> R>() {
    override fun apply(context: Context): Result<(R, R) -> R> =
        when (val o = this@binary.apply(context)) {
            is Success -> context.success(map(o.value), 0)
            is Error -> context.error("operator not found", 0 , subResults = listOf(o))
        }
}

fun <O, R> Parser<O>.unary(map : (O) -> (R) -> R) = object: Parser<(R) -> R>() {
    override fun apply(context: Context): Result<(R) -> R> =
        when (val o = this@unary.apply(context)) {
            is Success -> context.success(map(o.value), 0)
            is Error -> context.error("operator not found", 0 , subResults = listOf(o))
        }
}

fun <O, R> Parser<O>.nullary(map : (O) -> () -> R) = object: Parser<() -> R>() {
    override fun apply(context: Context): Result<() -> R> =
        when (val o = this@nullary.apply(context)) {
            is Success -> context.success(map(o.value), 0)
            is Error -> context.error("operator not found", 0 , subResults = listOf(o))
        }
}

fun <R> Parser<out R>.prefix(op: Parser<(R)->R>) = object: Parser<R>() {
    override fun apply(context: Context): Result<R> =
        when (val rs = oneOrMore(op).apply(context)) {
            is Success -> when (val r1 = this@prefix.apply(context)) {
                is Success -> {
                    context.success(rs.value.foldRight(r1.value) { o, r -> o(r)}, 0)
                }
                is Error -> context.error("prefix last parser failed", subResults = listOf(r1))

            }
            is Error -> when (val r1 = this@prefix.apply(context)) {
                is Success -> context.success(r1.value, 0)
                is Error -> context.error(r1.message, 0, listOf(r1))
            }
        }
}

fun <R> Parser<R>.solo() = object: Parser<R>() {
    override fun apply(context: Context): Result<R> =
        this@solo.apply(context)
}


fun <R> Parser<out R>.postfix(op: Parser<(R)->R>) = object: Parser<R>() {
    override fun apply(context: Context): Result<R> =
        when (val r1 = this@postfix.apply(context)) {
            is Success -> {
                when (val rs = oneOrMore(op).apply(context)) {
                    is Success -> {
                        val all = rs.value
                        context.success(all.fold(r1.value) { l, o -> o(l)}, 0)
                    }
                    is Error -> context.success(r1.value, 0)
                }
            }
            is Error -> context.error("postfix first parser failed", subResults = listOf(r1))
        }
}

fun <R> Parser<out R>.infixr(op: Parser<(R, R) -> R>) = object: Parser<R>() {
    private fun List<Pair<(R, R) -> R, R>>.go(l: R) : R {
        return when {
            this.isEmpty() -> l
            else -> {
                val (o, r) = first()
                val tail = drop(1)
                when {
                    tail.isNotEmpty() -> o(l, tail.go(r))
                    else -> o(l, r)
                }
            }
        }
    }

    override fun apply(context: Context): Result<R> =
        when (val r1 = this@infixr.apply(context)) {
            is Success -> {
                when (val rs = oneOrMore(seq(op, this@infixr)).apply(context)) {
                    is Success -> {
                        context.success(rs.value.go(r1.value), 0)
                    }
                    is Error -> context.success(r1.value, 0)
                }
            }
            is Error -> context.error("postfix first parser failed", subResults = listOf(r1))
        }
}

fun <R> Parser<out R>.infixl(op: Parser<(R, R) -> R>) = object: Parser<R>() {
    override fun apply(context: Context): Result<R> =
        when (val r1 = this@infixl.apply(context)) {
            is Success -> {
                when (val rs = oneOrMore(seq(op, this@infixl)).apply(context)) {
                    is Success -> {
                        context.success(rs.value.fold(r1.value) { l, (o, r) -> o(l, r) }, 0)
                    }
                    is Error -> context.success(r1.value , 0)
                }
            }
            is Error -> context.error("postfix first parser failed", subResults = listOf(r1))
        }
}

class OparatorTable<R> {
    val ops = mutableListOf<Operator>()
    companion object {
        fun <R> create(term: Parser<R>, init: OparatorTable<R>.DSL.() -> Unit): Parser<R> {
            val t = OparatorTable<R>()
            t.DSL().init()

            val sortedOps = t.ops.groupBy { it.precedence to it.associativity }.toSortedMap(compareBy({ -it.first}, { it.second})).toMutableMap()
            val operatorExpr = sortedOps.entries.fold(term) { operand, entry ->
                when (entry.key.second) {
                    Associativity.INFIXL -> operand.infixl(oneOf(*(entry.value.map { it.op as Parser<(R, R) -> R> }).toTypedArray()))
                    Associativity.INFIXR -> operand.infixr(oneOf(*(entry.value.map { it.op as Parser<(R, R) -> R> }).toTypedArray()))
                    Associativity.PREFIX -> operand.prefix(oneOf(*(entry.value.map { it.op as Parser<(R) -> R> }).toTypedArray()))
                    Associativity.POSTFIX -> operand.postfix(oneOf(*(entry.value.map { it.op as Parser<(R) -> R> }).toTypedArray()))
                    Associativity.SOLO -> operand.solo()

                }
            }
            return operatorExpr
        }
    }
    enum class Associativity{
        INFIXL, INFIXR, PREFIX, POSTFIX, SOLO
    }
    data class Operator(val op: Parser<*>, val precedence : Int, val associativity: Associativity)

    inner class DSL {
        fun infixl(precedence: Int, op: Parser<(R,R) -> R>) {
            ops.add(Operator(op, precedence, Associativity.INFIXL))
        }

        fun infixr(precedence: Int, op: Parser<(R,R) -> R>) {
            ops.add(Operator(op, precedence, Associativity.INFIXR))
        }

        fun prefix(precedence: Int, op: Parser<(R) -> R>) {
            ops.add(Operator(op, precedence, Associativity.PREFIX))
        }

        fun solo(precedence: Int, op: Parser<() -> R>) {
            ops.add(Operator(op, precedence, Associativity.SOLO))
        }

        fun postfix(precedence: Int, op: Parser<(R) -> R>) {
            ops.add(Operator(op, precedence, Associativity.POSTFIX))
        }

    }

}

