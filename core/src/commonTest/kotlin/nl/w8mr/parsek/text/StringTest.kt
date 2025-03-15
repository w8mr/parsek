package nl.w8mr.parsek.text

import io.kotest.matchers.shouldBe
import kotlin.js.JsName
import kotlin.test.Test

class StringTest {
    @Test
    @JsName("StringMatchesFixedString")
    fun `string matches fixed string`() {
        val parser = string("add")
        parser.parse("add 1") shouldBe "add"
    }
}