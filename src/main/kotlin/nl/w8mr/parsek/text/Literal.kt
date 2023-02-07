package nl.w8mr.parsek.text

import nl.w8mr.parsek.Context
import nl.w8mr.parsek.Parser

fun literal(literal: String) = object: Parser<Unit>() {
    override fun apply(context: Context): Result<Unit> {
        check(context.source is CharSequence) //TODO: Check how to handle better
        if (context.index > context.source.length) return context.error("End of File")
        val subSequence = context.source.subSequence(context.index, minOf(context.source.length, context.index + literal.length))
        val result = subSequence == literal
        return when (result) {
            true -> context.success(Unit, literal.length)
            false -> when (context.errorLevel) {
                3 -> {
                    val c1 = subSequence.toString().toCharArray()
                    val c2 = literal.toCharArray()
                    var i = 0
                    while (c1[i]==c2[i]) i++
                    when (i) {
                        0 -> context.error("$literal not found")
                        else -> context.error("partial match for $literal, ${literal.subSequence(0, i)} matched, expected character ${c2[i]} but found character ${c1[i]}", length = i)
                    }
                }
                else -> context.error("$literal not found")
            }
        }
    }
}

fun literal(literal: Char) = object: Parser<Unit>() {
    override fun apply(context: Context): Result<Unit> {
        check(context.source is CharSequence) //TODO: Check how to handle better
        if (context.index > context.source.length) return context.error("End of File")
        return when (context.source[context.index] == literal) {
            true -> context.success(Unit, 1)
            false -> context.error("$literal not found")
        }
    }
}
