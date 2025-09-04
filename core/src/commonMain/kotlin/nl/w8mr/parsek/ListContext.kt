package nl.w8mr.parsek

class ListContext<Token>(private val input: List<Token>, val index: Int = 0, state: Map<String, Any> = emptyMap()): AbstractContext<Token>(state) {

    override fun token(): Pair<Token, Context<Token>> {
        if (!hasNext()) throw NoSuchElementException("No more tokens")
        val token = input[index]
        val nextContext = ListContext(input, index + 1, state)
        return token to nextContext
    }

    override fun hasNext(): Boolean = index < input.size
    override fun index(): Long = index.toLong()

    override fun newContext(state: Map<String, Any>): ListContext<Token> = ListContext(input, index, state)
}