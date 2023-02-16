package nl.w8mr.parsek.text

import nl.w8mr.parsek.Parser
import nl.w8mr.parsek.map
import nl.w8mr.parsek.oneOf
import nl.w8mr.parsek.optional

object Parsers {
    val number get() = regex("-?\\d+") map { it.toInt() }

    fun zeroOrMore(parser: Parser<Char>) = nl.w8mr.parsek.repeat(parser).map { it.joinToString("") }
    fun oneOrMore(parser: Parser<Char>) = nl.w8mr.parsek.repeat(parser, min = 1).map { it.joinToString("") }

    @JvmName("seqcs")
    fun seq(p1: Parser<Char>, p2: Parser<String>) =
        nl.w8mr.parsek.seq(p1, p2) { v1, v2 -> "$v1$v2" }

    @JvmName("seqc")
    fun seq(p1: Parser<Char>) =
        p1.map { v1 -> "$v1" }

    @JvmName("seqcc")
    fun seq(p1: Parser<Char>, p2: Parser<Char>) =
        nl.w8mr.parsek.seq(p1, p2) { v1, v2 -> "$v1$v2" }

    @JvmName("seqsc")
    fun seq(p1: Parser<String>, p2: Parser<Char>) =
        nl.w8mr.parsek.seq(p1, p2) { v1, v2 -> "$v1$v2" }

    @JvmName("seqss")
    fun seq(p1: Parser<String>, p2: Parser<String>) =
        nl.w8mr.parsek.seq(p1, p2) { v1, v2 -> "$v1$v2" }

    @JvmName("seqcsc")
    fun seq(p1: Parser<Char>, p2: Parser<String>, p3: Parser<Char>) =
        nl.w8mr.parsek.seq(p1, p2, p3, { v1, v2, v3 -> "$v1$v2$v3" })

    @JvmName("seqcss")
    fun seq(p1: Parser<Char>, p2: Parser<String>, p3: Parser<String>) =
        nl.w8mr.parsek.seq(p1, p2, p3, { v1, v2, v3 -> "$v1$v2$v3" })

    infix fun <R> String.followedBy(parser: Parser<R>): Parser<R> = nl.w8mr.parsek.seq(literal(this), parser) { _, result -> result}
    infix fun <R> Parser<R>.followedBy(literal: String): Parser<R> = nl.w8mr.parsek.seq(this, literal(literal))  { result, _ -> result}

    infix fun <R> Char.followedBy(parser: Parser<R>): Parser<R> = nl.w8mr.parsek.seq(literal(this), parser) { _, result -> result}
    infix fun <R> Parser<R>.followedBy(literal: Char): Parser<R> = nl.w8mr.parsek.seq(this, literal(literal))  { result, _ -> result}

    @JvmName("orsc")
    infix fun Parser<String>.or(other: Parser<Char>) = oneOf(this, other.map { it.toString() })
    @JvmName("orcs")
    infix fun Parser<Char>.or(other: Parser<String>) = oneOf(this.map { it.toString() }, other)

    infix fun <R> Parser<R>.sepBy(separator: String) = nl.w8mr.parsek.zeroOrMore(
            nl.w8mr.parsek.seq(this, optional(literal(separator))
        ) { result, _ -> result })

}