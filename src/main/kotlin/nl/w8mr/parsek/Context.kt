package nl.w8mr.parsek

data class Context(val source: Any,
                   val errorLevel: Int = 1,
                   var index: Int = 0,
                   val recursionDetection: MutableSet<Pair<Parser<*>, Long>> = mutableSetOf()) {
    fun <R> error(message: String = "Unknown", length: Int = 0, subResults: List<Parser.Result<*>> = emptyList()): Parser.Result<R> {
        val index1 = index + length
        val example = when (source) {
            is CharSequence -> " (${
                source.subSequence(index1, minOf(10+index1, source.length))})"
            else -> ""

        }

        //println("ERROR: $message")
        return Parser.Error(
            "$message at position $index1$example",
            subResults
        )
    }

    fun <R> success(value: R, length: Int): Parser.Result<R> {
        index += length
        //println("SUCCES: $value")

        return Parser.Success(value)
    }

    fun hasNext() =
        when (source) {
            is CharSequence -> index < source.length
            is List<*> -> index < source.size
            else -> TODO()
        }

    fun peek() =
        when (source) {
            is CharSequence -> source[index]
            is List<*> -> source[index]
            else -> TODO()
        }

    var result: Any? = null
}
