package nl.w8mr.parsek

import nl.w8mr.parsek.text.literal

inline fun <Token, R> seq(vararg parsers: Parser<Token, out R>) = combi("{error}") {
    parsers.map { it.bind() }
}

inline fun <R1, R2, R, Token> seq(p1: Parser<Token, R1>, p2: Parser<Token, R2>, crossinline map: (v1: R1, v2: R2) -> R) =
    combi { map(p1.bind(), p2.bind()) }

inline fun <Token> seq(p1: LiteralParser<Token>, p2: LiteralParser<Token>) =
    literalCombi {
        p1.bind()
        p2.bind()
    }

inline fun <R, Token> seq(p1: LiteralParser<Token>, p2: Parser<Token, R>) =
    combi {
        p1.bind()
        p2.bind()
    }

inline fun <R, Token> seq(p1: Parser<Token, R>, p2: LiteralParser<Token>) =
    combi {
        val r = p1.bind()
        p2.bind()
        r
    }

inline fun <R1, R2, Token> seq(p1: Parser<Token, R1>, p2: Parser<Token, R2>) = seq(p1, p2, ::Pair)

inline fun <R1, R2, R3, R, Token> seq(p1: Parser<Token, R1>, p2: Parser<Token, R2>, p3: Parser<Token, R3>,
                                      crossinline map: (v1: R1, v2: R2, v3: R3) -> R) =
    combi { map(p1.bind(), p2.bind(), p3.bind()) }

inline fun <R1, R2, R3, R4, R, Token> seq(p1: Parser<Token, R1>, p2: Parser<Token, R2>,
                                          p3: Parser<Token, R3>, p4: Parser<Token, R4>,
                                          crossinline map: (v1: R1, v2: R2, v3: R3, v4: R4) -> R) =
    combi { map(p1.bind(), p2.bind(), p3.bind(), p4.bind()) }

inline fun <R1, R2, R3, R4, R5, R, Token> seq(p1: Parser<Token, R1>, p2: Parser<Token, R2>,
                                              p3: Parser<Token, R3>, p4: Parser<Token, R4>, p5: Parser<Token, R5>,
                                              crossinline map: (v1: R1, v2: R2, v3: R3, v4: R4, v5: R5) -> R) =
    combi { map(p1.bind(), p2.bind(), p3.bind(), p4.bind(), p5.bind()) }

inline fun <R1, R2, R3, R4, R5, R6, R, Token> seq(p1: Parser<Token, R1>, p2: Parser<Token, R2>,
                                                  p3: Parser<Token, R3>, p4: Parser<Token, R4>,
                                                  p5: Parser<Token, R5>, p6: Parser<Token, R6>,
                                                  crossinline map: (v1: R1, v2: R2, v3: R3, v4: R4, v5: R5, v6: R6) -> R) =
    combi { map(p1.bind(), p2.bind(), p3.bind(), p4.bind(), p5.bind(), p6.bind()) }

//infix fun <R, Token> Parser<Token, R>.and(other: Parser<Token, R>) = seq(this, other)
infix fun <R, Token> LiteralParser<Token>.and(other: Parser<Token, R>) = seq(this, other) { _, r -> r }
infix fun <R, Token> Parser<Token, R>.and(other: LiteralParser<Token>) = seq(this, other) { r, _ -> r }
infix fun <Token> LiteralParser<Token>.and(other: LiteralParser<Token>) = seq(this, other)
infix fun <R1, R2, Token> Parser<Token, R1>.and(other: Parser<Token, R2>) = seq(this, other)



