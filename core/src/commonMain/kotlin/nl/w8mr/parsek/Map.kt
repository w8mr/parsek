package nl.w8mr.parsek


private fun <Token, R, S> Parser<Token, R>._map(func: (R) -> (S)) = direct<Token, S> {
    this@_map.bindAsResult().map(func)
}

infix fun <Token, R, S> Parser<Token, R>.map(func: (R) -> (S)) = _map(func)
infix fun <Token, R1, R2, S> Parser<Token, Pair<R1, R2>>.map(func: (R1, R2) -> (S)) = _map { func(it.first, it.second) }
infix fun <Token, R1, R2, R3, S> Parser<Token, Pair<Pair<R1, R2>, R3>>.map(func: (R1, R2, R3) -> (S)) = _map { func(it.first.first, it.first.second, it.second) }
infix fun <Token, R1, R2, R3, R4, S> Parser<Token, Pair<Pair<Pair<R1, R2>, R3>, R4>>.map(func: (R1, R2, R3, R4) -> (S)) = _map { func(it.first.first.first, it.first.first.second, it.first.second, it.second) }

//TODO: change to using parser function
fun <Token, R> Parser<Token, R>.asLiteral(message: String = "{error}") = this.let {
    literalCombi<Token> {
        it.bind()
    }
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

infix fun <Token, R> LiteralParser<Token>.value(value: R) = (this as Parser<Token, Unit>).map { value }
infix fun <Token> LiteralParser<Token>.effect(func: () -> Unit) = literalCombi<Token>("{error}") {
    this@effect.bind()
    func()
}
