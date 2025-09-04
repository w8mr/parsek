package nl.w8mr.parsek

import kotlin.coroutines.cancellation.CancellationException

class ParseInteruptedException(override val message: String):
    CancellationException("kotlin.coroutines.cancellation.CancellationException should never get swallowed. Always re-throw it if captured.")

fun <Token, R> combi(message: String = "Combinator failed, parser number {index} with error: {error}", block: CombinatorDSL<Token, R>.() -> R) = object : Parser<Token, R> {
    override fun apply(context: Context<Token>): Pair<Parser.Result<R>, Context<Token>> = doApply(
        context,
        block,
        message
    )
}

fun <Token> literalCombi(message: String = "Combinator failed, parser number {index} with error: {error}", block: CombinatorDSL<Token, Unit>.() -> Unit) = object : LiteralParser<Token> {
    override fun apply(context: Context<Token>): Pair<Parser.Result<Unit>, Context<Token>> = doApply(
        context,
        block,
        message
    )
}

fun <Token, R> Parser<Token, R>.doApply(
    context: Context<Token>,
    block: CombinatorDSL<Token, R>.() -> R,
    message: String
): Pair<Parser.Result<R>, Context<Token>> {
    val subResults = mutableListOf<Parser.Result<*>>()
    return try {
        val dsl = ParserCombinatorDSL(this, context, subResults)
        val result = block.invoke(dsl)
        success(result, subResults) to dsl.context
    } catch (ex: ParseInteruptedException) {
        failure(message.replace("{index}", subResults.size.toString()).replace("{error}", ex.message), subResults) to context
    }
}


sealed interface CombinatorDSL<Token, R> {

    operator fun <S> Parser<Token, S>.unaryMinus(): S = bind()
    fun <S> Parser<Token, S>.bind(): S
    fun <S> Parser<Token, S>.bindAsResult(): Parser.Result<S>

    fun <S> Parser.Result<S>.bind(): S

    fun fail(error: Any): Nothing
    fun success(value: R, subResults: List<Parser.Result<*>> = emptyList()): Parser.Success<R>
    fun failure(message: String, subResults: List<Parser.Result<*>> = emptyList()): Parser.Failure

    fun stateGet(key: String): Any
    fun stateGetOrNull(key: String): Any?
    fun stateGetOrPut(key: String, defaultValue: () -> Any): Any
    fun statePut(key: String, value: Any)

    fun index(): Long
}

class ParserCombinatorDSL<Token, R> (private val parser: Parser<Token, R>, var context: Context<Token>, val subResults: MutableList<Parser.Result<*>>) : CombinatorDSL<Token, R> {
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

    override fun fail(error: Any) = throw ParseInteruptedException(error.toString())

    override fun success(value: R, subResults: List<Parser.Result<*>>): Parser.Success<R> = parser.success(value, subResults)
    override fun failure(message: String, subResults: List<Parser.Result<*>>): Parser.Failure = parser.failure(message, subResults)

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

