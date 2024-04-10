package nl.w8mr.parsek

abstract class Parser<R> {
    sealed class Result<R>(open val subResults: List<Result<*>> = emptyList())

    data class Success<R>(val value: R, override val subResults: List<Result<*>> = emptyList()) : Result<R>(subResults)

    data class Error<R>(val message: String, override val subResults: List<Result<*>> = emptyList()) : Result<R>(subResults)

    abstract fun apply(context: Context): Result<R>

    fun parse(
        source: Any,
        errorLevel: Int = 1,
    ): R =
        when (val result = apply(Context(source, errorLevel))) {
            is Success -> result.value
            is Error -> {
                println(result)
                throw ParseException(result.message, result)
            }
        }
}
