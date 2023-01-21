package nl.w8mr.parsek

abstract class Parser<T, S, R> {
    sealed class Result<R>(open val subResults: List<Result<*>> = emptyList())
    data class Success<R>(val value: R, override val subResults: List<Result<*>> = emptyList()) : Result<R>(subResults)
    data class Error<R>(val message: String, override val subResults: List<Result<*>> = emptyList()) : Result<R>(subResults)

    abstract fun apply(context: Context<T, S>): Result<R>

    fun parse(source: S, errorLevel: Int = 1): R =
        when (val result = apply(Context(source, errorLevel))) {
            is Success -> result.value
            is Error -> throw ParseException(result.message, result)
        }
}
