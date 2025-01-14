# Todo
- Better documentation
- Better imports
- Better separation of text parsers
- Long / BigInteger number parsers
- Success, Error/Failure handling
- Lazy subresults
- Error path vs Parse tree
- Parse tree without errors
- Char/String/Unit Parser (special handling and/seq/combi)
- 


Parsers:
```
fun <R1, R2, R> seq(p1: Parser<R1>, p2: Parser<R2>, map: (v1: R1, v2: R2) -> R) = object: Parser<R>() {
...
fun <R1, R2, R3, R4, R5, R6, R> seq(p1: Parser<R1>, p2: Parser<R2>, p3: Parser<R3>, p4: Parser<R4>, p5: Parser<R5>, p6: Parser<R6>, map: (v1: R1, v2: R2, v3: R3, v4: R4, v5: R5, v6: R6) -> R) =

fun <R1, R2> seq(p1: Parser<R1>, p2: Parser<R2>) =

infix fun <R: Any?> Parser<Unit>.prefixLiteral(parser : Parser<R>): Parser<R> = seq(this, parser) { _, r -> r}
infix fun <R: Any?> Parser<R>.postfixLiteral(parser : Parser<Unit>): Parser<R> = seq(this, parser) { r, _ -> r}



fun <R> Parser<R>.sepBy(
infix fun <R> Parser<R>.sepBy(separator: Parser<*>) = sepBy(separator, false)
infix fun <R> Parser<R>.sepByAllowEmpty(separator: Parser<*>) = sepBy(separator, true)


fun <R> ref(parserRef: KProperty0<Parser<R>>): Parser<R> =

OperatorTable

fun <R> lookAhead(parser: Parser<R>) = object: Parser<Unit>() {

Token / fun <R : Any> literal(literal: KClass<R>) =


until  / infix fun <R> Parser<*>.failOr(parser: Parser<R>): Parser<R> = object: Parser<R>() {

fun eof() = object: Parser<Unit>() {

infix fun <R> String.and(parser: Parser<R>): Parser<R> = seq(literal(this), parser) { _, result -> result}
infix fun <R> Parser<R>.and(literal: String): Parser<R> = seq(this, literal(literal))  { result, _ -> result}
infix fun <R> Parser<R>.and(parser: Parser<Unit>) = seq(this, parser)  { result, _ -> result}
infix fun <R1,R2> Parser<R1>.and(parser: Parser<R2>) = seq(this, parser)


fun regex(pattern: String) = object :  Parser<String>() {

Text

combi returning ParserChar/ParserString/ParserUnit

fun literal(literal: String) = object: Parser<Unit>() {
fun literal(literal: Char) = object: Parser<Unit>() {

infix fun <R> String.followedBy(parser: Parser<R>): Parser<R> = nl.w8mr.parsek.seq(literal(this), parser) { _, result -> result }
infix fun <R> Parser<R>.followedBy(literal: String): Parser<R> = nl.w8mr.parsek.seq(this, literal(literal)) { result, _ -> result }
infix fun <R> Char.followedBy(parser: Parser<R>): Parser<R> = nl.w8mr.parsek.seq(literal(this), parser) { _, result -> result }
infix fun <R> Parser<R>.followedBy(literal: Char): Parser<R> = nl.w8mr.parsek.seq(this, literal(literal)) { result, _ -> result }

infix fun Parser<String>.or(other: Parser<Char>) = oneOf(this, other.map { it.toString() })
infix fun Parser<Char>.or(other: Parser<String>) = oneOf(this.map { it.toString() }, other)
infix fun <R> Parser<R>.sepBy(separator: String) =


```
