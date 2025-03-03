import nl.w8mr.parsek.Parser
import nl.w8mr.parsek.oneOf
import nl.w8mr.parsek.text.char

infix fun Char.or(other: Parser<Char, String>) = oneOf(char(this), other)
infix fun Parser<Char, String>.or(other: Char) = oneOf(this, char(other))
infix fun Char.or(other: Char) = oneOf(char(this), char(other))
infix fun Parser<Char, String>.or(other: Parser<Char, String>) = oneOf(this, other)
