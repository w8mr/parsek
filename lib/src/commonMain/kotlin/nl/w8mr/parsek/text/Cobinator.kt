package nl.w8mr.parsek.text

import nl.w8mr.parsek.*

fun <Token> textCombi(message: String = "Combinator failed, parser number {index} with error: {error}", block: CombinatorDSL<Token, String>.() -> String) = object :
    TextParser<Token> {
    override fun applyImpl(source: ParserSource<Token>): Parser.Result<String> {
        val subResults = mutableListOf<Parser.Result<*>>()
        return try {
            val dsl = ParserCombinatorDSL(this, source, subResults)
            val result = block.invoke(dsl)
            success(result, subResults)
        } catch (ex: ParseInteruptedException) {
            failure(message.replace("{index}", subResults.size.toString()).replace("{error}", ex.message), subResults)
        }
    }
}
