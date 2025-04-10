package nl.w8mr.parsek.text

import io.kotest.matchers.shouldBe
import kotlin.js.JsName
import kotlin.test.Test

class SeqTest {
    @Test
    @JsName("LiteralAndTextparserAndLiteral")
    fun `literal and textparser and literal`() {
        val parser = '(' and digit and ')'
        parser.parse("(1)") shouldBe "1"

    }

    @Test
    @JsName("LiteralAndParserAndLiteral")
    fun `literal and parser and literal`() {
        val parser = '(' and number and ')'
        parser.parse("(12)") shouldBe 12

    }

    @Test
    @JsName("CharAndChar")
    fun `char and char`() {
        val parser = char('(') and digit and char(')')
        parser.parse("(1)") shouldBe "(1)"

    }


}