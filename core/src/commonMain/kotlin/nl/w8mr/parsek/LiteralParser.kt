package nl.w8mr.parsek

import kotlin.reflect.KClass

interface LiteralParser<Token> : Parser<Token, Unit>

fun <Token, R : Any> literal(kClass: KClass<R>) = simpleLiteral<Token> {
    val token = token()
    when {
        kClass.isInstance(token) -> Unit
        else -> fail("token is not instance of ${kClass.simpleName}")
    }
}