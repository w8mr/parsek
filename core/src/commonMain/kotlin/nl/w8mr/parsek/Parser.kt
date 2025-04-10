package nl.w8mr.parsek

import nl.w8mr.parsek.text.CharSequenceSource
import kotlin.reflect.KClass


interface Parser<Token, R> {
    sealed class Result<R>(open val subResults: List<Result<*>> = emptyList())

    data class Success<R>(val value: R, override val subResults: List<Result<*>> = emptyList()) : Result<R>(subResults)

    data class Failure<R>(val message: String, override val subResults: List<Result<*>> = emptyList()) : Result<R>(subResults)

    fun apply(source: ParserSource<Token>): Result<R> {
        val mark = source.mark()
        return when (val result = applyImpl(source)) {
            is Success -> {
                source.release(mark)
                result
            }
            is Failure -> {
                source.reset(mark)
                result
            }
        }
    }

    fun applyImpl(source: ParserSource<Token>): Result<R>

    fun success(value: R, subResults: List<Result<*>> = emptyList()) = Success(value, subResults)
    fun failure(message: String, subResults: List<Result<*>> = emptyList()) = Failure<R>(message, subResults)

    fun parse(source: ParserSource<Token>): R {
        return apply(source).let { result ->
            when (result) {
                is Success -> result.value
                is Failure -> throw ParseException(result.message, result)
            }
        }
    }

    fun parseTree(source: ParserSource<Token>): Pair<R?, Result<R>> {
        return apply(source).let { result ->
            when (result) {
                is Success -> result.value to result
                is Failure -> null to result
            }
        }
    }

}

fun <Token, R> Parser<Token, R>.fold(
    iterator: ParserSource<Token>,
    success: ((R, List<Parser.Result<*>>) -> Parser.Result<R>)? = null,
    failed: ((String, List<Parser.Result<*>>)-> Parser.Result<R>)? = null
) = when(val result = apply(iterator)) {
    is Parser.Success -> if (success==null) result else success(result.value, result.subResults)
    is Parser.Failure -> if (failed==null) result else failed(result.message, result.subResults)
}

interface ParserSource<Token> {
    fun next(): Token?
    fun hasNext(): Boolean
    var index: Int

    fun mark() : Int
    fun reset(mark: Int)
    fun release(mark: Int)

    val state: MutableMap<String, Any>


}

class ListSource<Token>(val input: List<Token>): ParserSource<Token> {
    override var index = 0
    override fun mark(): Int = index
    override fun reset(mark: Int) { index = mark }
    override fun release(mark: Int) = Unit
    override val state: MutableMap<String, Any> get() = mutableMapOf()

//    override fun peek(): kotlin.Char? = if (hasNext()) input[index] else null

    override fun next(): Token? = if (hasNext()) input[index++] else null

    override fun hasNext(): Boolean = index < input.size
}

fun <Token, R> Parser<Token, R>.parse(input: List<Token>) =
    this.parse(ListSource(input))

fun <Token, R : Any> token(kClass: KClass<R>) =
    object : Parser<Token, R> {
        override fun applyImpl(source: ParserSource<Token>): Parser.Result<R> {
            val mark = source.mark()
            val match = source.next()
            return when (kClass.isInstance(match)) {
                false -> {
                    source.reset(mark)
                    failure("token is not instance of ${kClass.simpleName}")
                }

                true -> {
                    source.release(mark)
                    success(match as R)
                }
            }
        }
    }



