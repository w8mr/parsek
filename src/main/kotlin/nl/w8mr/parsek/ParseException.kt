package nl.w8mr.parsek

class ParseException(override val message: String, val error: Parser.Error<*>) : Exception(message)