package nl.w8mr.parsek

inline fun <R1, R2, R, Token> seq(p1: Parser<R1, Token>, p2: Parser<R2, Token>, crossinline map: (v1: R1, v2: R2) -> R) =
    combi { map(p1.bind(), p2.bind()) }

inline fun <R1, R2, Token> seq(p1: Parser<R1, Token>, p2: Parser<R2, Token>) = seq(p1, p2, ::Pair)

inline fun <R1, R2, R3, R, Token> seq(p1: Parser<R1, Token>, p2: Parser<R2, Token>, p3: Parser<R3, Token>,
                                      crossinline map: (v1: R1, v2: R2, v3: R3) -> R) =
    combi { map(p1.bind(), p2.bind(), p3.bind()) }

inline fun <R1, R2, R3, R4, R, Token> seq(p1: Parser<R1, Token>, p2: Parser<R2, Token>,
                                          p3: Parser<R3, Token>, p4: Parser<R4, Token>,
                                          crossinline map: (v1: R1, v2: R2, v3: R3, v4: R4) -> R) =
    combi { map(p1.bind(), p2.bind(), p3.bind(), p4.bind()) }

inline fun <R1, R2, R3, R4, R5, R, Token> seq(p1: Parser<R1, Token>, p2: Parser<R2, Token>,
                                              p3: Parser<R3, Token>, p4: Parser<R4, Token>, p5: Parser<R5, Token>,
                                              crossinline map: (v1: R1, v2: R2, v3: R3, v4: R4, v5: R5) -> R) =
    combi { map(p1.bind(), p2.bind(), p3.bind(), p4.bind(), p5.bind()) }

inline fun <R1, R2, R3, R4, R5, R6, R, Token> seq(p1: Parser<R1, Token>, p2: Parser<R2, Token>,
                                                  p3: Parser<R3, Token>, p4: Parser<R4, Token>,
                                                  p5: Parser<R5, Token>, p6: Parser<R6, Token>,
                                                  crossinline map: (v1: R1, v2: R2, v3: R3, v4: R4, v5: R5, v6: R6) -> R) =
    combi { map(p1.bind(), p2.bind(), p3.bind(), p4.bind(), p5.bind(), p6.bind()) }

