package nl.w8mr.parsek

import kotlin.reflect.KClass

fun <T: Any, S: List<T>, R : T> literal(literal: KClass<R>) = object: Parser<T, S, R>() {
    override fun apply(context: Context<T, S>): Result<R> {
        if (!context.hasNext()) return context.error("End of File")
        val token = context.source[context.index]
        return when {
            literal.isInstance(token) -> context.success(token as R, 1)
            else -> context.error("$literal not found")
        }
    }
}
