package nl.w8mr.parsek.example.jafun

import nl.w8mr.parsek.literal
import nl.w8mr.parsek.sepBy
import nl.w8mr.parsek.seq
import nl.w8mr.parsek.zeroOrMore


val identifier_term = literal(Identifier::class)
val complexIdentifier = identifier_term sepBy literal(Dot::class)
val stringLiteral_term = literal(StringLiteral::class)
val invoke = seq(complexIdentifier, stringLiteral_term, ::Invocation)
val statement = invoke
val parser = zeroOrMore(statement)
