package nl.w8mr.parsek

import nl.w8mr.parsek.text.literal

infix fun <R> String.and(parser: Parser<Char, CharSequence, R>): Parser<Char, CharSequence, R> = seq(literal(this), parser) { _, result -> result}
infix fun <R> Parser<Char, CharSequence, R>.and(literal: String): Parser<Char, CharSequence, R> = seq(this, literal(literal))  { result, _ -> result}

@JvmName("andUnit")
infix fun <R> Parser<Char, CharSequence, R>.and(parser: Parser<Char, CharSequence, Unit>) = seq(this, parser)  { result, _ -> result}

infix fun <R1,R2> Parser<Char, CharSequence, R1>.and(parser: Parser<Char, CharSequence, R2>) = seq(this, parser)
