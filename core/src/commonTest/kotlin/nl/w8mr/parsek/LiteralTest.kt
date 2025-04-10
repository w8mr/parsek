package nl.w8mr.parsek

import io.kotest.matchers.shouldBe
import nl.w8mr.parsek.text.literal
import nl.w8mr.parsek.text.parse
import kotlin.js.JsName
import kotlin.test.Test

class LiteralTest {
    @Test
    @JsName("charLiteral")
    fun `char literal`() {
        val parser = literal('a')
        parser.parse("abc") shouldBe Unit
    }

    @Test
    @JsName("stringLiteral")
    fun `string literal`() {
        val parser = literal("abc")
        parser.parse("abcdef") shouldBe Unit
    }


}