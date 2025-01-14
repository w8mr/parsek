package nl.w8mr.parsek.text

import nl.w8mr.parsek.ParserSource
import nl.w8mr.parsek.Parser

class CharSequenceSource(val input: CharSequence): ParserSource<Char> {
    override var index = 0

//    override fun peek(): kotlin.Char? = if (hasNext()) input[index] else null

    override fun next(): kotlin.Char? = if (hasNext()) input[index++] else null

    override fun hasNext(): Boolean = index < input.length
}

