package nl.w8mr.parsek


fun <R, S, Token> Parser<R, Token>.map(message: String = "{error}", func: (R) -> (S)) = combi(message) {
    func(-this@map)
}

fun <R, S, Token> Parser<R, Token>.mapResult(message: String = "{error}", func: (Parser.Result<R>) -> (Parser.Result<S>)) = combi(message) {
    func(this@mapResult.bindResult()).bind()
}
infix fun <R, Token> Parser<R, Token>.filter(predicate: (value: R) -> Boolean): Parser<R, Token> = filter("Predicate not met", predicate)

fun <R, Token> Parser<R, Token>.filter(message: String, predicate: (value: R) -> Boolean) =
    mapResult {
        if ((it is Parser.Success) && (!predicate(it.value)))
            Parser.Error(message)
        else
            it
    }

