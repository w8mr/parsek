package nl.w8mr.parsek

import kotlin.reflect.KClass

interface LiteralParser<Token> : Parser<Token, Unit>

fun <Token, R : Any> literal(kClass: KClass<R>) =
    object : LiteralParser<Token> {
        override fun applyImpl(context: Context<Token>): Pair<Parser.Result<Unit>, Context<Token>> {
            val (token, new) = context.token()
            return when (kClass.isInstance(token)) {
                false -> {
                    failure("token is not instance of ${kClass.simpleName}") to context
                }

                true -> {
                    success(Unit) to new
                }
            }
        }
    }
