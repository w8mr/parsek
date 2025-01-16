package nl.w8mr.parsek.text

import nl.w8mr.parsek.Parser

fun <Token, R> Parser<Token, R>.map(message: String = "{error}", func: (R) -> String) =
    nl.w8mr.parsek.combi<Token, String>(message) {
        func(-this@map)
    }
