package nl.w8mr.parsek

import nl.w8mr.parsek.text.literal

infix fun <R> String.and(parser: Parser<R>): Parser<R> = seq(literal(this), parser) { _, result -> result}
infix fun <R> Parser<R>.and(literal: String): Parser<R> = seq(this, literal(literal))  { result, _ -> result}

@JvmName("andUnit")
infix fun <R> Parser<R>.and(parser: Parser<Unit>) = seq(this, parser)  { result, _ -> result}

infix fun <R1,R2> Parser<R1>.and(parser: Parser<R2>) = seq(this, parser)
