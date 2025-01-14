package nl.w8mr.parsek.text

import nl.w8mr.parsek.Parser

interface TextParser<R>: Parser<R, Char> {
}

fun <R> Parser<Char, R>.parse(input: CharSequence) =
    this.parse(CharSequenceSource(input))

fun <R> Parser<Char, R>.parseTree(input: CharSequence) =
    this.parseTree(CharSequenceSource(input))