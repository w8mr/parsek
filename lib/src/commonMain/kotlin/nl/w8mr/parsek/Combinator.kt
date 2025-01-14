package nl.w8mr.parsek

import kotlin.coroutines.cancellation.CancellationException

class ParseInteruptedException(override val message: String):
    CancellationException("kotlin.coroutines.cancellation.CancellationException should never get swallowed. Always re-throw it if captured.") {
}

fun <Token, R> combi(message: String = "Combinator failed, parser number {index} with error: {error}", block: CombinatorDSL<Token>.() -> R): Parser<Token, R> {
    return object : Parser<Token, R> {
        override fun apply(source: ParserSource<Token>): Parser.Result<R> {
            val subResults = mutableListOf<Parser.Result<*>>()
            return try {
                val dsl = ParserCombinatorDSL(source, subResults)
                val result = block.invoke(dsl)
                Parser.Success(result, subResults)
            } catch (ex: ParseInteruptedException) {
                Parser.Error(message.replace("{index}", subResults.size.toString()).replace("{error}", ex.message), subResults)
            }
        }
    }
}


sealed interface CombinatorDSL<Token> {
    operator fun <S> Parser<Token, S>.unaryMinus(): S
    fun <S> Parser<Token, S>.bind(): S
    fun <S> Parser<Token, S>.bindResult(): Parser.Result<S>

    fun <S> Parser.Result<S>.bind(): S

    fun <R> tryParser(block: ParserSource.TryDSLInterface.() -> Parser.Result<R>): Parser.Result<R>
    fun <R> tryParser(parser: Parser<Token, R>) = tryParser { parser.bindResult() }

    fun fail(error: String): Nothing
}

class ParserCombinatorDSL<Token>(private val source: ParserSource<Token>, val subResults: MutableList<Parser.Result<*>>) : CombinatorDSL<Token> {
    override inline fun <S> Parser<Token, S>.unaryMinus(): S = bind()

    override fun <S> Parser<Token, S>.bind(): S =
       this.apply(source).also(subResults::add).bind()

    override fun <S> Parser.Result<S>.bind() : S {
        return when (val result = this@bind) {
            is Parser.Success -> result.value
            is Parser.Error -> fail(result.message)
        }
    }

    override fun <R> tryParser(block: ParserSource.TryDSLInterface.() -> Parser.Result<R>) = source.tryParser(block)
    override fun <S> Parser<Token, S>.bindResult(): Parser.Result<S> =
        this.apply(source).also(subResults::add)

    override inline fun fail(error: String) = throw ParseInteruptedException(error)

}

