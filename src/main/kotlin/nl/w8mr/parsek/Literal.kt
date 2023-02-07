package nl.w8mr.parsek

import kotlin.reflect.KClass

fun <R: Any> literal(literal: KClass<R>) = object: Parser<R>() {
    override fun apply(context: Context): Result<R> {
        if (!context.hasNext()) return context.error("End of File")
        val token = context.peek()
        return when {
            literal.isInstance(token) -> context.success(token as R, 1)
            else -> context.error("$literal not found")
        }
    }
}
