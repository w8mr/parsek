package nl.w8mr.parsek

import kotlin.reflect.KProperty0

fun <R> ref(parserRef: KProperty0<Parser<R>>): Parser<R> =
    object : Parser<R>() {
        override fun apply(context: Context): Result<R> {
            val stateHash = 37L * context.index
            return when {
                context.recursionDetection.contains(Pair(this, stateHash)) ->
                    context.error("recursion detected")
                else -> {
                    context.recursionDetection.add(Pair(this, stateHash))
                    val result = parserRef.get().apply(context)
                    context.recursionDetection.remove(Pair(this, stateHash))
                    result
                }
            }
        }
    }
