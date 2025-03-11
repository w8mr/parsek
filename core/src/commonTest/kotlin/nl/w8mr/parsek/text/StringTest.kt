package nl.w8mr.parsek.text

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class StringTest {
    @Test
    fun `string matches fixed string`() {
        val parser = string("add")
        parser.parse("add 1") shouldBe "add"
    }
}