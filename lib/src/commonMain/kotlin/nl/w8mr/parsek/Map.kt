package nl.w8mr.parsek


fun <Token, R, S> Parser<Token, R>.map(message: String = "{error}", func: (R) -> (S)) = combi<Token, S>(message) {
    func(-this@map)
}

fun <Token, R, S> Parser<Token, R>.mapResult(message: String = "{error}", func: (Parser.Result<R>) -> (Parser.Result<S>)) = combi<Token, S>(message) {
    func(this@mapResult.bindAsResult()).bind()
}

infix fun <Token, R> Parser<Token, R>.filter(predicate: (value: R) -> Boolean): Parser<Token, R> = filter<Token, R>("Predicate not met", predicate)

fun <Token, R> Parser<Token, R>.filter(message: String, predicate: (value: R) -> Boolean) =
    mapResult {
        if ((it is Parser.Success) && (!predicate(it.value)))
            failure(message)
        else
            it
    }

