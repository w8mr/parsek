package nl.w8mr.parsek.text

import nl.w8mr.parsek.Parser

fun <Token, R> Parser<Token, R>.textMap(message: String = "{error}", func: (R) -> String) =
    textCombi<Token>(message) {
        func(-this@textMap)
    }
