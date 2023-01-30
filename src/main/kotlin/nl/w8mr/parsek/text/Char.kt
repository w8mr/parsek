package nl.w8mr.parsek.text

import nl.w8mr.parsek.Context
import nl.w8mr.parsek.Parser

fun char(char: Char) = object: Parser<Char, CharSequence, Char>() {
    override fun apply(context: Context<Char, CharSequence>): Result<Char> {
        if (context.index > context.source.length) return context.error("End of File")
        return when (val c = context.source[context.index]) {
            char -> context.success(c, 1)
            else -> context.error("$char not found")
        }
    }
}

fun char(msg: String? = null, predicate: (Char) -> Boolean) = object: Parser<Char, CharSequence, Char>() {
    override fun apply(context: Context<Char, CharSequence>): Result<Char> {
        if (context.index > context.source.length) return context.error("End of File")
        val c = context.source[context.index]
        return when {
            predicate(c) -> context.success(c, 1)
            else -> context.error("$c ${msg?:"does not match predicate"}")
        }
    }
}
