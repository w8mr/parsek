package nl.w8mr.parsek.text

import nl.w8mr.parsek.AbstractContext
import nl.w8mr.parsek.Parser

class CharSequenceContext(val input: CharSequence, val index: Int = 0, state: Map<String, Any> = emptyMap()): AbstractContext<Char>(state) {

    override fun token(): Pair<Char, CharSequenceContext> {
        if (!hasNext()) throw NoSuchElementException("No more tokens")
        val token = input[index]
        val nextContext = CharSequenceContext(input, index + 1, state)
        return token to nextContext
    }

    override fun hasNext(): Boolean = index < input.length
    override fun index(): Long = index.toLong()

    override fun newContext(state: Map<String, Any>): CharSequenceContext = CharSequenceContext(input, index, state)

}

fun <R> Parser<Char, R>.parse(input: CharSequence) =
    this.parse(CharSequenceContext(input))

fun <R> Parser<Char, R>.parseTree(input: CharSequence) =
    this.parseTree(CharSequenceContext(input))
