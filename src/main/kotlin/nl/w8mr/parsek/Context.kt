package nl.w8mr.parsek

data class Context<T, S>(val source: S, val errorLevel: Int = 1, var index: Int = 0) {
    fun <R> error(message: String = "Unknown", length: Int = 0, subResults: List<Parser.Result<*>> = emptyList()): Parser.Result<R> {
        val index1 = index + length
        val example = when (source) {
            is CharSequence -> " (${
                (source as CharSequence).subSequence(index1, minOf(10+index1, (source as CharSequence).length))})"
            else -> ""

        }

        return Parser.Error(
            "$message at position $index1$example",
            subResults
        )
    }

    fun <R> success(value: R, length: Int): Parser.Result<R> {
        index += length
        return Parser.Success(value)
    }

    fun hasNext() =
        when (source) {
            is CharSequence -> index < source.length
            is List<*> -> index < source.size
            else -> TODO()
        }
    var result: Any? = null
}
