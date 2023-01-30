package nl.w8mr.parsek.text

import nl.w8mr.parsek.example.jafun.WS
import nl.w8mr.parsek.example.jafun.lexer
import nl.w8mr.parsek.example.jafun.parser
import kotlin.test.Test

class CodeTest {

    @Test
    fun helloWorldLexer() {
        val input = "println \"Hello World\"\r\n"
        println(lexer.parse(input))
    }

    @Test
    fun complexHelloWorldLexer() {
        val input = "java.lang.System.out.println \"Hello World\"\r\n"
        println(lexer.parse(input))
    }


    @Test
    fun helloWorldParser() {
        val input = "println \"Hello World\"\r\n"
        val lexed = lexer.parse(input).filter { it !is WS }
        val parsed = parser.parse(lexed)
        println(parsed)
    }

    @Test
    fun complexHelloWorldParser() {
        val input = "java.lang.System.out.println \"Hello World\"\r\n"
        val lexed = lexer.parse(input).filter { it !is WS }
        val parsed = parser.parse(lexed)
        println(parsed)
    }

}