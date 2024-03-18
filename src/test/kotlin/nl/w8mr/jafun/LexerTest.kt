package nl.w8mr.jafun.nl.w8mr.jafun

import nl.w8mr.jafun.lexer
import nl.w8mr.jafun.test
import kotlin.test.Test
import kotlin.test.assertEquals

class LexerTest {

    @Test
    fun helloWorldLexer() {
        val input = "println \"Hello World\"\r\n"
        println(lexer.parse(input))
    }

    @Test
    fun integerLexer() {
        val input = "println 12_234_2\r\n"
        println(lexer.parse(input))
    }

    @Test
    fun twoArgFunLexer() {
        val input = "join(\"Hello\", \"World\")\r\n"
        println(lexer.parse(input))
    }

    @Test
    fun funDeclaration() {
        val input = """fun test() {}
        """.trimIndent()
        println(lexer.parse(input))
    }

}