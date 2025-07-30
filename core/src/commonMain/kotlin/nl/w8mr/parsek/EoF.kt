package nl.w8mr.parsek

fun <Token> eof() =
    object : Parser<Token, Unit> {
        override fun applyImpl(source: ParserSource<Token>): Parser.Result<Unit> {
            return when (source.hasNext()) {
                true -> failure("Token is not end of file")
                false -> success(Unit)
            }
        }
    }

