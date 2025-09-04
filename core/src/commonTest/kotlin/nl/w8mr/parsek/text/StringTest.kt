package nl.w8mr.parsek.text

import io.kotest.matchers.shouldBe
import nl.w8mr.parsek.ParseException
import nl.w8mr.parsek.test.shouldThrowMessage
import kotlin.js.JsName
import kotlin.test.Test

class StringTest {
    @Test
    @JsName("StringMatchesFixedString")
    fun `string matches fixed string`() {
        val parser = string("add")
        parser.parse("add 1") shouldBe "add"
    }

    @Test
    @JsName("StringMatchesFixedStringEoT")
    fun `string matches fixed string EoT`() {
        val parser = string("add")
        shouldThrowMessage<ParseException>("No more tokens available") {
            parser.parse("ad") shouldBe "add"
        }
    }

    @Test
    @JsName("StringMatchesFixedStringPartial")
    fun `string matches fixed string partial`() {
        val parser = string("add")
        shouldThrowMessage<ParseException>("Character c does not meet expected d, partial match: ad") {
            parser.parse("adc 1") shouldBe "add"
        }
    }

}