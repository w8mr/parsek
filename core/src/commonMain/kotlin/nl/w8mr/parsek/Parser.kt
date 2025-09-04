package nl.w8mr.parsek

import kotlin.reflect.KClass

interface Parser<Token, R> {

    sealed class Result<out R>(open val subResults: List<Result<*>> = emptyList())

    data class Success<R>(val value: R, override val subResults: List<Result<*>> = emptyList()) : Result<R>(subResults)

    data class Failure(val error: Any, override val subResults: List<Result<*>> = emptyList()) : Result<Nothing>(subResults)

    fun apply(context: Context<Token>): Pair<Result<R>, Context<Token>> {
        val (result, new) = applyImpl(context)
        return when (result) {
            is Success -> result to new
            is Failure -> result to context
        }
    }

    fun applyImpl(context: Context<Token>): Pair<Result<R>, Context<Token>>

    fun success(value: R, subResults: List<Result<*>> = emptyList()) = Success(value, subResults)
    fun failure(message: Any, subResults: List<Result<*>> = emptyList()) = Failure(message, subResults)

    fun parse(context: Context<Token>): R {
        return apply(context).let { (result, new) ->
            when (result) {
                is Success -> result.value
                is Failure -> throw ParseException(result.error.toString(), result)
            }
        }
    }

    fun parseTree(context: Context<Token>): Pair<R?, Parser.Result<R>> {
        return apply(context).let { (result, new) ->
            when (result) {
                is Success -> result.value to result
                is Failure -> null to result
            }
        }
    }

}

fun <Token, R> Parser<Token, R>.fold(
    context: Context<Token>,
    success: ((R, List<Parser.Result<*>>) -> Parser.Result<R>)? = null,
    failed: ((Any, List<Parser.Result<*>>) -> Parser.Result<R>)? = null
): Parser.Result<R> {
    val (result, new) = apply(context)
    return when (result) {
        is Parser.Success -> if (success == null) result else success(result.value, result.subResults)
        is Parser.Failure -> if (failed == null) result else failed(result.error, result.subResults)
    }
}

fun <Token, R> Parser<Token, R>.parse(input: List<Token>) =
    this.parse(ListContext(input))

fun <Token,  Type: Context<Token>, R : Any> token(kClass: KClass<R>) = object : Parser<Token, R> {
        override fun applyImpl(context: Context<Token>): Pair<Parser.Result<R>, Context<Token>> {
            val (token, new) = context.token()
            return when (token as? R) {
                null -> {
                    failure("token is not instance of ${kClass.simpleName}") to context
                }

                else -> {
                    success(token) to new
                }
            }
        }
    }



