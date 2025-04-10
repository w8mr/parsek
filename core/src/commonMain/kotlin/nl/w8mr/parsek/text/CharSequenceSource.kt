package nl.w8mr.parsek.text

import nl.w8mr.parsek.ParserSource
import nl.w8mr.parsek.Parser

class CharSequenceSource(val input: CharSequence): ParserSource<Char> {
    override var index = 0
    override fun mark(): Int = index
    override fun reset(mark: Int) { index = mark }
    override fun release(mark: Int) = Unit
    override val state: MutableMap<String, Any> get() = mutableMapOf()

//    override fun peek(): kotlin.Char? = if (hasNext()) input[index] else null

    override fun next(): Char? = if (hasNext()) input[index++] else null

    override fun hasNext(): Boolean = index < input.length
}

fun <R> Parser<Char, R>.parse(input: CharSequence) =
    this.parse(CharSequenceSource(input))

fun <R> Parser<Char, R>.parseTree(input: CharSequence) =
    this.parseTree(CharSequenceSource(input))
