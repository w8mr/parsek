package nl.w8mr.parsek.example.jafun

import nl.w8mr.parsek.map
import nl.w8mr.parsek.oneOf
import nl.w8mr.parsek.zeroOrMore
import nl.w8mr.parsek.text.Parsers.seq
import nl.w8mr.parsek.text.Parsers.zeroOrMore
import nl.w8mr.parsek.text.char
import java.lang.Character.isLetter


val underscore = char('_')
val unicode_lu = char(" is not Unicode upper case letter") { it.category == CharCategory.LOWERCASE_LETTER }
val unicode_ll = char(" is not Unicode lower case letter") { it.category == CharCategory.UPPERCASE_LETTER }
val unicode_lm = char(" is not Unicode modifier letter") { it.category == CharCategory.MODIFIER_LETTER }
val unicode_lt = char(" is not Unicode title case Letter") { it.category == CharCategory.TITLECASE_LETTER }
val unicode_lo = char(" is not Unicode other Letter") { it.category == CharCategory.OTHER_LETTER }
val unicode_digit = char(" is not Unicode digit") { it.category == CharCategory.DECIMAL_DIGIT_NUMBER }

val letter = char(" is not Unicode Letter", ::isLetter)
// val letter = oneOf(unicode_lu, unicode_ll, unicode_lm, unicode_lt, unicode_lo)
val identifier = seq(oneOf(letter, underscore), zeroOrMore(oneOf(letter, underscore, unicode_digit))).map(::Identifier)

val dot = char('.').map { Dot }
val lineStringContent = zeroOrMore(char(" is not valid string Char") { it != '"' && it != '\\'})
val lineStringLiteral = seq(char('"'), lineStringContent, char('"')).map(::StringLiteral)
val ws = char(" is not whitespace") { it == '\u0020' || it == '\u0009' || it == '\u000c' }.map { WS }
val lexer = zeroOrMore(oneOf(identifier, dot, lineStringLiteral, ws))