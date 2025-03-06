# Todo
- Char/String/Unit Parser (special handling and/seq/combi)
- Lazy parser // until parser?
- Lazy subresults
- Better documentation
- Long / BigInteger number parsers
- Error path vs Parse tree
- Parse tree without errors
- Extract parse function from Parser implementation



Parsers:
```
infix fun <R> Parser<R>.sepByAllowEmpty(separator: Parser<*>) = sepBy(separator, true)


OperatorTable

fun <R> lookAhead(parser: Parser<R>) = object: Parser<Unit>() {

Token / fun <R : Any> literal(literal: KClass<R>) =


until  / infix fun <R> Parser<*>.failOr(parser: Parser<R>): Parser<R> = object: Parser<R>() {

fun eof() = object: Parser<Unit>() {


fun regex(pattern: String) = object :  Parser<String>() {

Text

fun literal(literal: String) = object: Parser<Unit>() {

```
