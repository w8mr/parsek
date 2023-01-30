package nl.w8mr.parsek.example.jafun


sealed class Token
object Dot : Token()
object WS : Token()

data class Identifier(val value: String) : Token()
data class StringLiteral(val value: String) : Token()


sealed class Statement
data class Invocation(val method: List<Identifier>, val argument: StringLiteral): Statement()