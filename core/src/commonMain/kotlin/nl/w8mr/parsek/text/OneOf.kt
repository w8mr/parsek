package nl.w8mr.parsek.text

import nl.w8mr.parsek.LiteralParser
import nl.w8mr.parsek.oneOf

infix fun Char.or(other: LiteralParser<Char>) = oneOf(literal(this), other)
infix fun LiteralParser<Char>.or(other: Char) = oneOf(this, literal(other))
infix fun Char.or(other: Char) = oneOf(literal(this), literal(other))
