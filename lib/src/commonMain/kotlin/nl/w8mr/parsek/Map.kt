package nl.w8mr.parsek


fun <R, S, Token> Parser<Token, R>.map(message: String = "{error}", func: (R) -> (S)) = combi(message) {
    func(-this@map)
}

fun <R, S, Token> Parser<Token, R>.mapResult(message: String = "{error}", func: (Parser.Result<R>) -> (Parser.Result<S>)) = combi(message) {
    func(this@mapResult.bindAsResult()).bind()
}
infix fun <Token, R> Parser<Token, R>.filter(predicate: (value: R) -> Boolean): Parser<Token, R> = filter("Predicate not met", predicate)

fun <Token, R> Parser<Token, R>.filter(message: String, predicate: (value: R) -> Boolean) =
    mapResult {
        if ((it is Parser.Success) && (!predicate(it.value)))
            failure(message)
        else
            it
    }

