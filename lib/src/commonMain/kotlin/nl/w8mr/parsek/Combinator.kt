package nl.w8mr.parsek

import kotlin.coroutines.cancellation.CancellationException

class ParseInteruptedException(override val message: String):
    CancellationException("kotlin.coroutines.cancellation.CancellationException should never get swallowed. Always re-throw it if captured.")

fun <Token, R> combi(message: String = "Combinator failed, parser number {index} with error: {error}", block: CombinatorDSL<Token, R>.() -> R) = object : Parser<Token, R> {
        override fun applyImpl(source: ParserSource<Token>): Parser.Result<R> {
            val subResults = mutableListOf<Parser.Result<*>>()
            return try {
                val dsl = ParserCombinatorDSL(this, source, subResults)
                val result = block.invoke(dsl)
                success(result, subResults)
            } catch (ex: ParseInteruptedException) {
                failure(message.replace("{index}", subResults.size.toString()).replace("{error}", ex.message), subResults)
            }
        }
    }

fun <Token> literalCombi(message: String = "Combinator failed, parser number {index} with error: {error}", block: CombinatorDSL<Token, Unit>.() -> Unit) = object : LiteralParser<Token> {
    override fun applyImpl(source: ParserSource<Token>): Parser.Result<Unit> {
        val subResults = mutableListOf<Parser.Result<*>>()
        return try {
            val dsl = ParserCombinatorDSL(this, source, subResults)
            val result = block.invoke(dsl)
            success(result, subResults)
        } catch (ex: ParseInteruptedException) {
            failure(message.replace("{index}", subResults.size.toString()).replace("{error}", ex.message), subResults)
        }
    }
}


sealed interface CombinatorDSL<Token, R> {
    operator fun <S> Parser<Token, S>.unaryMinus(): S
    fun <S> Parser<Token, S>.bind(): S
    fun <S> Parser<Token, S>.bindAsResult(): Parser.Result<S>

    fun <S> Parser.Result<S>.bind(): S

    fun fail(error: String): Nothing
    fun success(value: R, subResults: List<Parser.Result<*>> = emptyList()): Parser.Success<R>
    fun failure(message: String, subResults: List<Parser.Result<*>> = emptyList()): Parser.Failure<R>

    fun mark() : Int
    fun reset(mark: Int)
    fun release(mark: Int)
    val state : MutableMap<String, Any>
}

class ParserCombinatorDSL<Token, R>(private val parser: Parser<Token, R>, private val source: ParserSource<Token>, val subResults: MutableList<Parser.Result<*>>) : CombinatorDSL<Token, R> {
    override fun <S> Parser<Token, S>.unaryMinus(): S = bind()

    override fun <S> Parser<Token, S>.bind(): S =
       this.apply(source).also(subResults::add).bind()

    override fun <S> Parser.Result<S>.bind() : S {
        return when (val result = this@bind) {
            is Parser.Success -> result.value
            is Parser.Failure -> fail(result.message)
        }
    }

    override fun <S> Parser<Token, S>.bindAsResult(): Parser.Result<S> =
        this.apply(source).also(subResults::add)

    override fun fail(error: String) = throw ParseInteruptedException(error)

    override fun success(value: R, subResults: List<Parser.Result<*>>): Parser.Success<R> = parser.success(value, subResults)
    override fun failure(message: String, subResults: List<Parser.Result<*>>): Parser.Failure<R> = parser.failure(message, subResults)

    override fun mark() = source.mark()
    override fun reset(mark: Int) = source.reset(mark)
    override fun release(mark: Int) = source.release(mark)

    override val state: MutableMap<String, Any> by source::state

}

