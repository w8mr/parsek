package nl.w8mr.parsek

class ListContext<Token>(private val input: List<Token>, val idx: Int = 0, state: Map<String, Any> = emptyMap()): AbstractContext<Token>(state) {

    override fun token(): Pair<Token, Context<Token>> {
        if (!hasNext()) throw NoSuchElementException("No more tokens")
        val token = input[idx]
        val nextContext = ListContext(input, idx + 1, state)
        return token to nextContext
    }

    override fun hasNext(): Boolean = idx < input.size
    override fun index(): Long = idx.toLong()

    override fun newContext(state: Map<String, Any>): ListContext<Token> = ListContext(input, idx, state)
}