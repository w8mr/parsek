package nl.w8mr.parsek

import kotlin.reflect.KProperty0

fun <R> ref(parserRef: KProperty0<Parser<R>>): Parser<R> = object : Parser<R>() {
    override fun apply(context: Context): Result<R> {
        return parserRef.get().apply(context)
    }

}
