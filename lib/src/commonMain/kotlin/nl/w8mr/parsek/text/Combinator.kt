package nl.w8mr.parsek.text

import nl.w8mr.parsek.*

fun <Token> textCombi(message: String = "Combinator failed, parser number {index} with error: {error}", block: CombinatorDSL<Token, String>.() -> String) = object :
    TextParser<Token> {
    override fun applyImpl(source: ParserSource<Token>) = doApply(source, block, message)

}
