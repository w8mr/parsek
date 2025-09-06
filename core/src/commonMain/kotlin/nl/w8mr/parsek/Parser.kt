package nl.w8mr.parsek

import nl.w8mr.parsek.Parser.Failure
import nl.w8mr.parsek.Parser.Success
import kotlin.reflect.KClass

interface Parser<Token, R> {

    sealed class Result<out R>(open val subResults: List<Result<*>> = emptyList())

    data class Success<R>(val value: R, override val subResults: List<Result<*>> = emptyList()) : Result<R>(subResults)

    data class Failure(val error: Any, override val subResults: List<Result<*>> = emptyList()) : Result<Nothing>(subResults)

    fun apply(context: Context<Token>): Pair<Result<R>, Context<Token>>

    fun success(value: R, subResults: List<Result<*>> = emptyList()) = Success(value, subResults)
    fun failure(error: Any, subResults: List<Result<*>> = emptyList()) = Failure(error, subResults)

}

fun <Token, R> Parser<Token, R>.fold(
    context: Context<Token>,
    success: ((R, List<Parser.Result<*>>) -> Parser.Result<R>)? = null,
    failed: ((Any, List<Parser.Result<*>>) -> Parser.Result<R>)? = null
) = apply(context).let { (result, _) ->
    when (result) {
        is Parser.Success -> if (success == null) result else success(result.value, result.subResults)
        is Parser.Failure -> if (failed == null) result else failed(result.error, result.subResults)
    }
}

fun <Token, R> Parser<Token, R>.parse(context: Context<Token>) = apply(context).let { (result, _) ->
    when (result) {
        is Success -> result.value to result
        is Failure -> null to result
    }
}

operator fun <Token, R> Parser<Token, R>.invoke(context: Context<Token>) = apply(context).let { (result, _) ->
    when (result) {
        is Success -> result.value
        is Failure -> throw ParseException(result.error.toString(), result)
    }
}



fun <Token, R> Parser<Token, R>.parse(input: List<Token>) =
    this.parse(ListContext(input))

operator fun <Token, R> Parser<Token, R>.invoke(input: List<Token>) =
    this(ListContext(input))

fun <Token: Any, R : Token> token(kClass: KClass<R>) = simple<Token, R> {
    val token = token()
    when {
        kClass.isInstance(token) -> token as? R ?: fail("token is not instance of ${kClass.simpleName}")
        else -> fail("token is not instance of ${kClass.simpleName}")
    }
}
