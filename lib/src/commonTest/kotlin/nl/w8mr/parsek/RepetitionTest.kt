import io.kotest.matchers.shouldBe
import nl.w8mr.parsek.*
import kotlin.test.Test

import nl.w8mr.parsek.text.*
import nl.w8mr.parsek.test.shouldThrowMessage

class RepetitionTest {
    @Test
    fun `repeat letter`() {
        val parser = repeat(letter)
        parser.parse("abc123") shouldBe listOf('a','b','c')
    }

    @Test
    fun `repeat with maximum`() {
        val parser = repeat(letter, 2)
        parser.parse("abc123") shouldBe listOf('a','b')
    }

    @Test
    fun `repeat with minimum`() {
        val parser = repeat(letter, min = 2)
        parser.parse("abc123") shouldBe listOf('a','b','c')
    }

    @Test
    fun `repeat with minimum failing`() {
        val parser = repeat(letter, min = 4)
        shouldThrowMessage<ParseException>("Repeat only 3 elements found, needed at least 4") {
            parser.parse("abc123")

        }
    }

    @Test
    fun `some letters`() {
        val parser = some(letter).asString
        parser.parse("abc123") shouldBe "abc"
    }

    @Test
    fun `some letters tree`() {
        val parser = some(letter)
        val full = parser.parseTree("abc123")
        full.first shouldBe listOf('a','b','c')
        full.second.subResults[0] shouldBe Parser.Success('a')
        full.second.subResults[1] shouldBe Parser.Success('b')
        full.second.subResults[2] shouldBe Parser.Success('c')
        full.second.subResults[3] shouldBe Parser.Error<Char>("Character 1 is not a letter", emptyList())
    }

    @Test
    fun `some letters asString tree`() {
        val parser = some(letter).asString
        val full = parser.parseTree("abc123")
        full.first shouldBe "abc"
        full.second.subResults[0] shouldBe Parser.Success(listOf('a','b','c'), listOf(
            Parser.Success('a'),
            Parser.Success('b'),
            Parser.Success('c'),
            Parser.Error<Char>("Character 1 is not a letter"),
        ))
    }

    @Test
    fun `some letters failing`() {
        val parser = some(letter).asString
        shouldThrowMessage<ParseException>("Repeat only 0 elements found, needed at least 1") {
            parser.parse("123")
        }
    }

    @Test
    fun `zeroOrMore empty`() {
        val parser = zeroOrMore(letter).asString
        parser.parse("123") shouldBe ""
    }

    @Test
    fun `times prefix`() {
        val parser = (4*letter).asString
        parser.parse("abcdef") shouldBe "abcd"
    }

    @Test
    fun `times postfix`() {
        val parser = (digit*4).asString
        parser.parse("123456") shouldBe "1234"
    }

    @Test
    fun `times range`() {
        val parser = ((2..4)*letter).asString
        parser.parse("abcdef") shouldBe "abcd"
        shouldThrowMessage<ParseException>("Repeat only 1 elements found, needed at least 2") {
            parser.parse("a12345")

        }
    }

    @Test
    fun `some combination`() {
        val parser = combi {
            val letters = -some(letter).asString
            val digits = -some(digit).asString
            letters to digits
        }
        parser.parse("abc123") shouldBe ("abc" to "123")
    }


}

