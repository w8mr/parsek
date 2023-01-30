package nl.w8mr.parsek.text

import nl.w8mr.parsek.Parser
import nl.w8mr.parsek.map
import nl.w8mr.parsek.oneOf
import nl.w8mr.parsek.optional

object Parsers {
    val number get() = regex("-?\\d+") map { it.toInt() }

    fun zeroOrMore(parser: CharParser<Char>) = nl.w8mr.parsek.repeat(parser).map { it.joinToString("") }

    @JvmName("seqcs")
    fun seq(p1: CharParser<Char>, p2: CharParser<String>) =
        nl.w8mr.parsek.seq(p1, p2) { v1, v2 -> "$v1$v2" }

    @JvmName("seqcc")
    fun seq(p1: CharParser<Char>, p2: CharParser<Char>) =
        nl.w8mr.parsek.seq(p1, p2) { v1, v2 -> "$v1$v2" }

    @JvmName("seqsc")
    fun seq(p1: CharParser<String>, p2: CharParser<Char>) =
        nl.w8mr.parsek.seq(p1, p2) { v1, v2 -> "$v1$v2" }

    @JvmName("seqss")
    fun seq(p1: CharParser<String>, p2: CharParser<String>) =
        nl.w8mr.parsek.seq(p1, p2) { v1, v2 -> "$v1$v2" }

    @JvmName("seqss")
    fun seq(p1: CharParser<Char>, p2: CharParser<String>, p3: CharParser<Char>) =
        nl.w8mr.parsek.seq(p1, p2, p3, { v1, v2, v3 -> "$v1$v2$v3" })


    @JvmName("orsc")
    infix fun CharParser<String>.or(other: CharParser<Char>) = oneOf(this, other.map { it.toString() })
    @JvmName("orcs")
    infix fun CharParser<Char>.or(other: CharParser<String>) = oneOf(this.map { it.toString() }, other)

    infix fun <R> CharParser<R>.sepBy(separator: String) = nl.w8mr.parsek.zeroOrMore(
            nl.w8mr.parsek.seq(this, optional(literal(separator))
        ) { result, _ -> result })

}