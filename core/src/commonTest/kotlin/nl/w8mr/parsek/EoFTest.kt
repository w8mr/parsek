package nl.w8mr.parsek

import io.kotest.matchers.shouldBe
import nl.w8mr.parsek.test.shouldThrowMessage
import nl.w8mr.parsek.text.char
import nl.w8mr.parsek.text.parse
import nl.w8mr.parsek.text.zeroOrMore
import kotlin.js.JsName
import kotlin.test.Test

class EoFTest {
    @Test
    @JsName("eofFound")
    fun `EoF found`() {
        val parser = zeroOrMore(char) and eof<Char>()
        parser.parse("abc") shouldBe ("abc" to Unit)
    }

    @Test
    @JsName("eofNotFound")
    fun `Eof not found`() {
        val parser = char and eof<Char>()
        shouldThrowMessage<ParseException>("Combinator failed, parser number 2 with error: Token is not end of file") {
            parser.parse("ab")
        }
    }


}