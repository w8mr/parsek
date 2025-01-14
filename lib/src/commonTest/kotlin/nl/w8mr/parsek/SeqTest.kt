package nl.w8mr.parsek


import nl.w8mr.parsek.*
import nl.w8mr.parsek.text.*

import io.kotest.matchers.shouldBe
import kotlin.test.Test
import nl.w8mr.parsek.test.shouldThrowMessage

class SeqTest {
    @Test
    fun `sequence of two as pair`() {
        val parser = seq(char, digit)
        parser.parse("a1b2c3d4") shouldBe ('a' to '1')
    }

    @Test
    fun `sequence of two`() {
        val parser = seq(char, digit) { a, b -> listOf(a, b) }
        parser.parse("a1b2c3d4") shouldBe listOf('a', '1')
    }

    @Test
    fun `sequence of three`() {
        val parser = seq(char, digit, char) { a, b, c -> listOf(a, b, c) }
        parser.parse("a1b2c3d4") shouldBe listOf('a', '1', 'b')
    }

    @Test
    fun `sequence of four`() {
        val parser = seq(char, digit, char, digit) { a, b, c, d -> listOf(a, b, c, d) }
        parser.parse("a1b2c3d4") shouldBe listOf('a', '1', 'b', '2')
    }

    @Test
    fun `sequence of five`() {
        val parser = seq(char, digit, char, digit, char) { a, b, c, d, e -> listOf(a, b, c, d, e) }
        parser.parse("a1b2c3d4") shouldBe listOf('a', '1', 'b', '2', 'c')
    }

    @Test
    fun `sequence of six`() {
        val parser = seq(char, digit, char, digit, char, digit) { a, b, c, d, e, f -> listOf(a, b, c, d, e, f) }
        parser.parse("a1b2c3d4") shouldBe listOf('a', '1', 'b', '2', 'c', '3')
    }

    @Test
    fun `sequence of six failing`() {
        val parser = seq(char, digit, char, digit, char, digit) { a, b, c, d, e, f -> listOf(a, b, c, d, e, f) }
        shouldThrowMessage<ParseException>("Combinator failed, parser number 4 with error: Character c is not a digit") {
            parser.parse("a1bc3d4")

        }
    }

    @Test
    fun `sequence of six failing parseTree`() {
        val parser = seq(char, digit, char, digit, char, digit) { a, b, c, d, e, f -> listOf(a, b, c, d, e, f) }
        val full = parser.parseTree("a1bc3d4")
        full.first shouldBe null
        full.second shouldBe Parser.Error("Combinator failed, parser number 4 with error: Character c is not a digit",
            listOf(
                Parser.Success('a'),
                Parser.Success('1'),
                Parser.Success('b'),
                Parser.Error("Character c is not a digit"),
            )
        )
    }

}