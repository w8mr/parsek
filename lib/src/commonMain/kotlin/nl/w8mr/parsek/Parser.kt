package nl.w8mr.parsek


interface Parser<Token, R> {
    sealed class Result<R>(open val subResults: List<Result<*>> = emptyList())

    data class Success<R>(val value: R, override val subResults: List<Result<*>> = emptyList()) : Result<R>(subResults)

    data class Error<R>(val message: String, override val subResults: List<Result<*>> = emptyList()) : Result<R>(subResults)

    fun apply(iterator: ParserSource<Token>): Result<R>

    fun parse(source: ParserSource<Token>): R {
        return apply(source).let { result ->
            when (result) {
                is Success -> result.value
                is Error -> throw ParseException(result.message, result)
            }
        }
    }

    fun parseTree(source: ParserSource<Token>): Pair<R?, Parser.Result<R>> {
        return apply(source).let { result ->
            when (result) {
                is Success -> result.value to result
                is Error -> null to result
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
    is Parser.Error -> if (failed==null) result else failed(result.message, result.subResults)
}

interface ParserSource<Token> {
//    fun peek(): Token?
    fun next(): Token?
    fun hasNext(): Boolean
    var index: Int

    fun <R> tryParser(block: TryDSLInterface.() -> Parser.Result<R>): Parser.Result<R> {
        val mark = index
        return when (val result = block.invoke(TryDSL(this, index))) {
            is Parser.Error -> {
                index = mark
                result
            }
            else -> result
        }
    }

    interface TryDSLInterface {
    }

    class TryDSL<Token>(val source: ParserSource<Token>, val mark: Int) : TryDSLInterface {
    }
}

