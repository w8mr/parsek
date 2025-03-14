package nl.w8mr.parsek.text

import nl.w8mr.parsek.LiteralParser
import nl.w8mr.parsek.Parser
import nl.w8mr.parsek.oneOf
import nl.w8mr.parsek.text.literal

infix fun Char.or(other: LiteralParser<Char>) = oneOf(literal(this), other)
infix fun LiteralParser<Char>.or(other: Char) = oneOf(this, literal(other))
infix fun Char.or(other: Char) = oneOf(literal(this), literal(other))
//infix fun <Token, R> Parser<Token, out R>.or(other: Parser<Token, out R>) = oneOf(this, other)
infix fun <Token, R> LiteralParser<Token>.or(other: LiteralParser<Token>) = oneOf(this, other)
