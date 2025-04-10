package nl.w8mr.parsek


import nl.w8mr.parsek.text.*

import io.kotest.matchers.shouldBe
import kotlin.test.Test
import nl.w8mr.parsek.test.shouldThrowMessage
import kotlin.js.JsName

class SeqTest {
    @Test
    @JsName("SequenceOfTwoAsPair")
    fun `sequence of two as pair`() {
        val parser = seq(char, digit)
        parser.parse("a1b2c3d4") shouldBe ("a" to "1")
    }

    @Test
    @JsName("SequenceOfTwo")
    fun `sequence of two`() {
        val parser = seq(char, digit) { a, b -> listOf(a, b) }
        parser.parse("a1b2c3d4") shouldBe listOf("a", "1")
    }

    @Test
    @JsName("SequenceOfThree")
    fun `sequence of three`() {
        val parser = seq(char, digit, char) { a, b, c -> listOf(a, b, c) }
        parser.parse("a1b2c3d4") shouldBe listOf("a", "1", "b")
    }

    @Test
    @JsName("SequenceOfFour")
    fun `sequence of four`() {
        val parser = seq(char, digit, char, digit) { a, b, c, d -> listOf(a, b, c, d) }
        parser.parse("a1b2c3d4") shouldBe listOf("a", "1", "b", "2")
    }

    @Test
    @JsName("SequenceOfFive")
    fun `sequence of five`() {
        val parser = seq(char, digit, char, digit, char) { a, b, c, d, e -> listOf(a, b, c, d, e) }
        parser.parse("a1b2c3d4") shouldBe listOf("a", "1", "b", "2", "c")
    }

    @Test
    @JsName("SequenceOfSix")
    fun `sequence of six`() {
        val parser = seq(char, digit, char, digit, char, digit) { a, b, c, d, e, f -> listOf(a, b, c, d, e, f) }
        parser.parse("a1b2c3d4") shouldBe listOf("a", "1", "b", "2", "c", "3")
    }

    @Test
    @JsName("SequenceOfSixFailing")
    fun `sequence of six failing`() {
        val parser = seq(char, digit, char, digit, char, digit) { a, b, c, d, e, f -> listOf(a, b, c, d, e, f) }
        shouldThrowMessage<ParseException>("Combinator failed, parser number 4 with error: Character c is not a digit") {
            parser.parse("a1bc3d4")

        }
    }

    @Test
    @JsName("SequenceOfSixFailingParseTree")
    fun `sequence of six failing parseTree`() {
        val parser = seq(char, digit, char, digit, char, digit) { a, b, c, d, e, f -> listOf(a, b, c, d, e, f) }
        val full = parser.parseTree("a1bc3d4")
        full.first shouldBe null
        full.second shouldBe Parser.Failure("Combinator failed, parser number 4 with error: Character c is not a digit",
            listOf(
                Parser.Success("a"),
                Parser.Success("1"),
                Parser.Success("b"),
                Parser.Failure("Character c is not a digit"),
            )
        )
    }

    @Test
    @JsName("VarargsShortSequence")
    fun `varargs short sequence`() {
        val parser = seq(char, digit, char)
        parser.parse("a1b2c3d4") shouldBe listOf("a", "1", "b")
    }

    @Test
    @JsName("VarargsLongSequence")
    fun `varargs long sequence`() {
        val parser = seq(char, digit, char, digit, char, digit, char, digit)
        parser.parse("a1b2c3d4") shouldBe listOf("a", "1", "b", "2", "c", "3", "d", "4")
    }

}