package nl.w8mr.parsek.text

import nl.w8mr.parsek.Parser
import nl.w8mr.parsek.seq

infix fun <R> Char.and(other: Parser<Char, R>) = seq(char(this), other) { _, r -> r}
infix fun <R> Parser<Char, R>.and(other: Char) = seq(this, char(other)) { r, _ -> r}