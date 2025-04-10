package nl.w8mr.zipdok

import nl.w8mr.parsek.*
import nl.w8mr.parsek.text.char
import nl.w8mr.parsek.text.literal
import nl.w8mr.parsek.text.Parsers.followedBy
import java.io.File
fun main() {
    val file = File("lib/src/commonMain/kotlin/parsek/text/Character.kt")
    val text = file.readText()

    val startline = zeroOrMore(literal("<!--- ZIPDOK ") map { null } or char { it != '\n' }) map { it.filterNotNull().joinToString("")}
    val command = (zeroOrMore(char { it != '\n' } followedBy '\n')) map { it.joinToString("")}
    val line = oneOf(seq(startline, command), (zeroOrMore(char { it != '\n' }) followedBy '\n') map { null})
    val parser = zeroOrMore(line) map { it.filterNotNull() }
    println(zeroOrMore(startline followedBy '\n').parse(text))

}