package nl.w8mr.parsek

import kotlin.coroutines.cancellation.CancellationException

class ParseInteruptedException(val error: Any):
    CancellationException("nl.w8mr.parsek.ParseInteruptedException should never get swallowed. Always re-throw it if captured.")

inline fun <Token, R> simple(message: String = "{error}", crossinline block: SimpleDSL<Token>.() -> R) = object : Parser<Token, R> {
    override fun apply(context: Context<Token>): Pair<Parser.Result<R>, Context<Token>> = doSimple(
        context,
        block,
        message
    )
}

inline fun <Token> simpleLiteral(message: String = "{error}", crossinline block: SimpleDSL<Token>.() -> Unit) = object : LiteralParser<Token> {
    override fun apply(context: Context<Token>): Pair<Parser.Result<Unit>, Context<Token>> = doSimple(
        context,
        block,
        message
    )
}


inline fun <Token, R> combi(message: String = "Combinator failed, parser number {index} with error: {error}", crossinline block: CombinatorDSL<Token>.() -> R) = object : Parser<Token, R> {
    override fun apply(context: Context<Token>): Pair<Parser.Result<R>, Context<Token>> = doApply(
        context,
        block,
        message
    )
}

inline fun <Token> literalCombi(message: String = "Combinator failed, parser number {index} with error: {error}", crossinline block: CombinatorDSL<Token>.() -> Unit) = object : LiteralParser<Token> {
    override fun apply(context: Context<Token>): Pair<Parser.Result<Unit>, Context<Token>> = doApply(
        context,
        block,
        message
    )
}

inline fun <Token, R> Parser<Token, R>.doSimple(
    context: Context<Token>,
    crossinline block: SimpleDSL<Token>.() -> R,
    message: String
): Pair<Parser.Result<R>, Context<Token>> {
    return try {
        val dsl = ParserSimpleDSL(context)
        val result = block.invoke(dsl)
        success(result, emptyList()) to dsl.context
    } catch (ex: ParseInteruptedException) {
        failure((ex.error as? String)?.let { message.replace("{error}", it)}  ?: ex.error, emptyList()) to context
    }
}


inline fun <Token, R> Parser<Token, R>.doApply(
    context: Context<Token>,
    crossinline block: CombinatorDSL<Token>.() -> R,
    message: String
): Pair<Parser.Result<R>, Context<Token>> {
    val subResults = mutableListOf<Parser.Result<*>>()
    return try {
        val dsl = ParserCombinatorDSL(context, subResults)
        val result = block.invoke(dsl)
        success(result, subResults) to dsl.context
    } catch (ex: ParseInteruptedException) {
        failure((ex.error as? String)?.let { message.replace("{index}", subResults.size.toString()).replace("{error}", it)}  ?: ex.error, subResults) to context
    }
}

inline fun <Token, R> direct(crossinline block: CombinatorDSL<Token>.() -> Parser.Result<R>) = object : Parser<Token, R> {
    override fun apply(context: Context<Token>): Pair<Parser.Result<R>, Context<Token>> {
        return try {
            val dsl = ParserCombinatorDSL(context, mutableListOf())
            val result = block.invoke(dsl)
            result to dsl.context
        }
        catch (ex: ParseInteruptedException) {
            failure(ex.error) to context
        }
    }
}

sealed interface SimpleDSL<Token> {
    fun hasToken(): Boolean
    fun token(): Token
    fun fail(error: Any): Nothing

    fun stateGet(key: String): Any
    fun stateGetOrNull(key: String): Any?
    fun stateGetOrPut(key: String, defaultValue: () -> Any): Any
    fun statePut(key: String, value: Any)

    fun index(): Long
}

sealed interface CombinatorDSL<Token>: SimpleDSL<Token> {

    operator fun <S> Parser<Token, S>.unaryMinus(): S = bind()
    fun <S> Parser<Token, S>.bind(): S
    fun <S> Parser<Token, S>.bindAsResult(): Parser.Result<S>

    fun <S, T> Parser.Result<S>.map(f: (S) -> T): Parser.Result<T>

    fun <S> Parser.Result<S>.bind(): S

}

open class ParserSimpleDSL<Token> (var context: Context<Token>) : SimpleDSL<Token> {
    override fun hasToken(): Boolean = context.hasNext()

    override fun token(): Token {
        when (context.hasNext()) {
            false -> fail("No more tokens available")
            true -> {
                val (token, new) = context.token()
                context = new
                return token
            }
        }
    }

    override fun fail(error: Any) = throw ParseInteruptedException(error)

    override fun stateGet(key: String) = context.stateGet(key).first

    override fun stateGetOrNull(key: String) = context.stateGetOrNull(key).first

    override fun stateGetOrPut(key: String, defaultValue: () -> Any): Any {
        val (value, new) = context.stateGetOrPut(key, defaultValue)
        context = new
        return value
    }

    override fun statePut(key: String, value: Any) {
        context = context.statePut(key, value)
    }

    override fun index(): Long = context.index()

}

class ParserCombinatorDSL<Token> (context: Context<Token>, private val subResults: MutableList<Parser.Result<*>>) : ParserSimpleDSL<Token>(context), CombinatorDSL<Token> {
    override fun <S> Parser<Token, S>.bind(): S = bindAsResult().bind()

    override fun <S> Parser.Result<S>.bind() : S {
        return when (val result = this@bind) {
            is Parser.Success -> result.value
            is Parser.Failure -> fail(result.error)
        }
    }

    override fun <S> Parser<Token, S>.bindAsResult(): Parser.Result<S>  {
        val (result, new) = this.apply(context)
        subResults += result
        context = new
        return result
    }

    override fun <S, T> Parser.Result<S>.map(f: (S) -> T): Parser.Result<T> = when (this) {
        is Parser.Success<S> -> Parser.Success(f(this.value), this.subResults)
        is Parser.Failure -> this
    }
}

