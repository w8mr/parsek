package nl.w8mr.jafun

import nl.w8mr.jafun.Token.*
import nl.w8mr.parsek.map
import nl.w8mr.parsek.oneOf
import nl.w8mr.parsek.text.Parsers.oneOrMore
import nl.w8mr.parsek.or
import nl.w8mr.parsek.text.Parsers.followedBy
import nl.w8mr.parsek.text.Parsers.seq
import nl.w8mr.parsek.text.Parsers.zeroOrMore
import nl.w8mr.parsek.text.char
import nl.w8mr.parsek.text.literal
import nl.w8mr.parsek.zeroOrMore
import java.lang.Character.isLetter

val operatorSymbols = listOf('!', '#', '$', '%', '*', '+', '<', '>', '?', '\\', '/', '^', '|', ':', '-', '~')

val underscore = char('_')
val unicode_digit = char(" is not Unicode digit") { it.category == CharCategory.DECIMAL_DIGIT_NUMBER }

val letter = char(" is not Unicode Letter", ::isLetter)
val normalIdentifier = seq(oneOf(letter, underscore), zeroOrMore(oneOf(letter, underscore, unicode_digit))).map(::Identifier)
val operatorIdentifier = oneOrMore(char { it in operatorSymbols }).map { Identifier(it, true) }
val identifier = normalIdentifier or operatorIdentifier
val decimalDigit = char { it in '0'..'9'}
val decimalDigitNoZero = char { it in '1'..'9'}
val decimalDigitOrSeparator = oneOf(decimalDigit, char('_'))
val integerLiteral = oneOf(seq(decimalDigitNoZero, zeroOrMore(decimalDigitOrSeparator)), seq(decimalDigit)).map { IntegerLiteral(it.replace("_","").toInt()) }

val dot = char('.').map { Dot }
val lParen = char('(').map { LParen }
val rParen = char(')').map { RParen }
val lCurl = char('{').map { LCurl }
val rCurl = char('}').map { RCurl }
val comma = char(',').map { Comma }
val assignment = char('=').map { Assignment }
val val_token = literal("val").map { Val }
val fun_token = literal("fun").map { Fun }

val lineStringContent = zeroOrMore(char(" is not valid string Char") { it != '"' && it != '\\' })
val lineStringLiteral = ('"' followedBy lineStringContent followedBy '"').map(::StringLiteral)

val ws = char(" is not whitespace") { it == '\u0020' || it == '\u0009' || it == '\u000c' }.map { WS }
val newline = oneOf(seq(char('\n')),  seq(char('\r'), char(('\n')))).map { Newline }

val lexer = zeroOrMore(oneOf(fun_token, val_token, identifier, operatorIdentifier, dot, lineStringLiteral, integerLiteral, ws, newline, lCurl, rCurl, lParen, rParen, comma, assignment))
