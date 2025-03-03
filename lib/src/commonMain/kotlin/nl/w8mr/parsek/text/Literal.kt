package nl.w8mr.parsek.text

import nl.w8mr.parsek.LiteralParser
import nl.w8mr.parsek.Parser
import nl.w8mr.parsek.ParserSource

fun literal(expected: Char, message: String = "Character {actual} does not meet expected {expected}") = object :
    LiteralParser<Char> {
    override fun applyImpl(source: ParserSource<Char>): Parser.Result<Unit> = when (val char = source.next()) {
        expected -> success(Unit)
        else -> failure(message.replace("{actual}", char.toString()).replace("{expected}", expected.toString()))
    }
}