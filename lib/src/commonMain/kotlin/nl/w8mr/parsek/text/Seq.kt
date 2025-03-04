package nl.w8mr.parsek.text

import nl.w8mr.parsek.*

infix fun <R> Char.and(other: Parser<Char, R>) = seq(literal(this), other)
infix fun <R> Parser<Char, R>.and(other: Char) = seq(this, literal(other))
infix fun Parser<Char, String>.and(other: Parser<Char, String>) = seq(this, other) { a, b -> "$a$b"}

fun any(parser: LiteralParser<Char>): LiteralParser<Char> = repeat(parser).asLiteral()
