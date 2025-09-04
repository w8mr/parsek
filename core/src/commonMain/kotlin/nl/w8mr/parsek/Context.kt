package nl.w8mr.parsek

interface Context<Token> {
    fun token(): Pair<Token, Context<Token>>
    fun hasNext(): Boolean

    fun stateGet(key: String): Pair<Any, Context<Token>>
    fun stateGetOrNull(key: String): Pair<Any?, Context<Token>>
    fun stateGetOrPut(key: String, defaultValue: () -> Any): Pair<Any, Context<Token>>
    fun statePut(key: String, value: Any): Context<Token>

    fun index(): Long
}

abstract class AbstractContext<Token>(val state: Map<String, Any> = mapOf()): Context<Token> {

    override fun stateGet(key: String): Pair<Any, Context<Token>> = state.getValue(key) to this

    override fun stateGetOrNull(key: String): Pair<Any?, Context<Token>> = state[key] to this

    override fun stateGetOrPut(key: String, defaultValue: () -> Any): Pair<Any, Context<Token>> = when {
        state.containsKey(key) -> state.getValue(key) to this
        else -> {
            val value = defaultValue()
            val newContext = statePut(key, value)
            value to newContext
        }
    }

    abstract fun newContext(state: Map<String, Any>): Context<Token>
    override fun statePut(key: String, value: Any): Context<Token> {
        val newState = (state.toList() + (key to value)).toMap()
        return newContext(newState)
    }
}