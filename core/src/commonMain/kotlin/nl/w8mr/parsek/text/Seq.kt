package nl.w8mr.parsek.text

import nl.w8mr.parsek.*

infix fun Char.and(other: Char) = seq(literal(this), literal(other))
infix fun String.and(other: Char) = seq(literal(this), literal(other))
infix fun Char.and(other: String) = seq(literal(this), literal(other))
infix fun String.and(other: String) = seq(literal(this), literal(other))
infix fun <R> Char.and(other: Parser<Char, R>) = seq(literal(this), other)
infix fun <R> String.and(other: Parser<Char, R>) = seq(literal(this), other)
infix fun  Char.and(other: LiteralParser<Char>) = seq(literal(this), other)
infix fun  String.and(other: LiteralParser<Char>) = seq(literal(this), other)
infix fun <R> Parser<Char, R>.and(other: Char) = seq(this, literal(other))
infix fun <R> Parser<Char, R>.and(other: String) = seq(this, literal(other))
infix fun  LiteralParser<Char>.and(other: Char) = seq(this, literal(other))
infix fun  LiteralParser<Char>.and(other: String) = seq(this, literal(other))
infix fun  Parser<Char, String>.and(other: Parser<Char, String>) = seq(this, other) { a, b -> "$a$b"}

