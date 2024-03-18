package nl.w8mr.jafun.nl.w8mr.jafun

import nl.w8mr.jafun.Parser
import nl.w8mr.jafun.Token
import nl.w8mr.jafun.lexer
import kotlin.test.Test

class ParserTest {

    @Test
    fun helloWorldParser() {
        val input = "println \"Hello World\" \r\n"
        val lexed = lexer.parse(input).filter { it !is Token.WS }
        println(lexed)
        println(Parser.parse(lexed))
    }

    @Test
    fun integerParser() {
        val input = "println 7\r\n"
        val lexed = lexer.parse(input).filter { it !is Token.WS }
        println(lexed)
        println(Parser.parse(lexed))
    }

    @Test
    fun helloWorldNormalParser() {
        val input = "println(\"Hello World\")\r\n"
        val lexed = lexer.parse(input).filter { it !is Token.WS }
        println(lexed)
        println(Parser.parse(lexed))
    }

    @Test
    fun helloWorldTwoLevelParser() {
        val input = """
            println(join("Hello","world"))
        """.trimIndent()
        val lexed = lexer.parse(input).filter { it !is Token.WS }
        println(lexed)
        println(Parser.parse(lexed))
    }
    @Test
    fun twoArgFunParser() {
        val input = "join(\"Hello\", \"World\")\r\n"
        val lexed = lexer.parse(input).filter { it !is Token.WS }
        println(lexed)
        println(Parser.parse(lexed))
    }

    @Test
    fun complexHelloWorldParser() {
        val input = "java.lang.System.out.println \"Hello World\"\r\n"
        val lexed = lexer.parse(input).filter { it !is Token.WS }
        println(lexed)
        println(Parser.parse(lexed))
    }

    @Test
    fun assignment() {
        val input = "val str = \"Hello World\"\r\nprintln str\r\n"
        val lexed = lexer.parse(input).filter { it !is Token.WS }
        println(lexed)
        println(Parser.parse(lexed))
    }

    @Test
    fun emptyFunction() {
        val input = """
            fun test() { }
        """.trimIndent()
        val lexed = lexer.parse(input).filter { it !is Token.WS }
        println(lexed)
        println(Parser.parse(lexed))
    }

    @Test
    fun filledFunction() {
        val input = """
            fun test() { println 1 + 2 }
        """.trimIndent()
        val lexed = lexer.parse(input).filter { it !is Token.WS }
        println(lexed)
        println(Parser.parse(lexed))
    }

    @Test
    fun functionInFunction() {
        val input = """
            fun test() { fun inner() { println 1 + 2 } }
        """.trimIndent()
        val lexed = lexer.parse(input).filter { it !is Token.WS }
        println(lexed)
        println(Parser.parse(lexed))
    }

}