package nl.w8mr.parsek.text

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class SeqTest {
    @Test
    fun `char and textparser and char`() {
        val parser = '(' and digit and ')'
        parser.parse("(1)") shouldBe "1"

    }

    @Test
    fun `char and parser and char`() {
        val parser = '(' and number and ')'
        parser.parse("(12)") shouldBe 12

    }

}