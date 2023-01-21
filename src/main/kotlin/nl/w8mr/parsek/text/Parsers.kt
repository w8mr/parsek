package nl.w8mr.parsek.text

import nl.w8mr.parsek.map

object Parsers {
    val number get() = regex("-?\\d+") map { it.toInt() }
}