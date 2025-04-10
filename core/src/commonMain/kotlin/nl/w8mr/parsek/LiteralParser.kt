package nl.w8mr.parsek

import kotlin.reflect.KClass

interface LiteralParser<Token> : Parser<Token, Unit>

fun <Token, R : Any> literal(kClass: KClass<R>) =
    object : LiteralParser<Token> {
        override fun applyImpl(source: ParserSource<Token>): Parser.Result<Unit> {
            val mark = source.mark()
            return when (kClass.isInstance(source.next())) {
                false -> {
                    source.reset(mark)
                    failure("token is not instance of ${kClass.simpleName}")
                }

                true -> {
                    source.release(mark)
                    success(Unit)
                }
            }
        }
    }
