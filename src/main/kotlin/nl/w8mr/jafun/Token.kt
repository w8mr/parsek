package nl.w8mr.jafun

sealed interface Token {
    object Dot : Token
    object LParen : Token
    object RParen : Token
    object LCurl : Token
    object RCurl : Token
    object Comma : Token
    object Assignment : Token
    object Val : Token
    object Fun : Token
    object WS : Token
    object Newline : Token


    data class Identifier(val value: String, val operator: Boolean = false) : Token
    data class StringLiteral(val value: String) : Token
    data class IntegerLiteral(val value: Int) : Token
}

