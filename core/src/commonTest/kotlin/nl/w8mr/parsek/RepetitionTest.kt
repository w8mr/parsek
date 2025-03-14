import io.kotest.matchers.shouldBe
import nl.w8mr.parsek.*
import kotlin.test.Test

import nl.w8mr.parsek.text.*
import nl.w8mr.parsek.test.shouldThrowMessage

class RepetitionTest {
    @Test
    fun `repeat letter`() {
        val parser = repeat(letter)
        parser.parse("abc123") shouldBe "abc"
    }

    @Test
    fun `repeat with maximum`() {
        val parser = repeat(letter, 2)
        parser.parse("abc123") shouldBe "ab"
    }

    @Test
    fun `repeat with minimum`() {
        val parser = repeat(letter, min = 2)
        parser.parse("abc123") shouldBe "abc"
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
        val parser = some(letter)
        parser.parse("abc123") shouldBe "abc"
    }

    @Test
    fun `some letters tree`() {
        val parser = some(letter)
        val full = parser.parseTree("abc123")
        full.first shouldBe "abc"
        full.second shouldBe Parser.Success("abc", listOf(
            Parser.Success("a"),
            Parser.Success("b"),
            Parser.Success("c"),
            Parser.Failure<Char>("Character 1 is not a letter")
        ))
    }

    @Test
    fun `some letters asString tree`() {
        val parser = some(letter)
        val full = parser.parseTree("abc123")
        full.first shouldBe "abc"
        full.second shouldBe Parser.Success("abc", listOf(
            Parser.Success("a"),
            Parser.Success("b"),
            Parser.Success("c"),
            Parser.Failure("Character 1 is not a letter"),
        ))
    }

    @Test
    fun `some letters failing`() {
        val parser = some(letter)
        shouldThrowMessage<ParseException>("Repeat only 0 elements found, needed at least 1") {
            parser.parse("123")
        }
    }

    @Test
    fun `zeroOrMore empty`() {
        val parser = zeroOrMore(letter)
        parser.parse("123") shouldBe ""
    }

    @Test
    fun `times prefix`() {
        val parser = (4*letter)
        parser.parse("abcdef") shouldBe "abcd"
    }

    @Test
    fun `times postfix`() {
        val parser = (digit*4)
        parser.parse("123456") shouldBe "1234"
    }

    @Test
    fun `times range`() {
        val parser = ((2..4)*letter)
        parser.parse("abcdef") shouldBe "abcd"
        shouldThrowMessage<ParseException>("Repeat only 1 elements found, needed at least 2") {
            parser.parse("a12345")

        }
    }

    @Test
    fun `some combination`() {
        val parser = combi {
            val letters = -some(letter)
            val digits = -some(digit)
            letters to digits
        }
        parser.parse("abc123") shouldBe ("abc" to "123")
    }

    @Test
    fun `untilLazy`() {
        val parser = untilLazy(letter, char(','))
        parser.parse("abc,123,abc") shouldBe ("abc" to ",")
    }

    @Test
    fun `untilLazy failing`() {
        val parser = untilLazy(letter, char(','))
        shouldThrowMessage<ParseException>("Stop not found") {
            parser.parse("abc123,123,abc")
        }
    }


    @Test
    fun `untilLazy less then minimum`() {
        val parser = untilLazy(letter, char(','), min = 4)
        shouldThrowMessage<ParseException>("Repeat only 3 elements found, needed at least 4") {
            parser.parse("abc,123,abc")
        }
    }

    @Test
    fun `untilLazy over minimum`() {
        val parser = untilLazy(letter, char(','), min = 2)
        parser.parse("abc,123,abc") shouldBe ("abc" to ",")

    }

    @Test
    fun `untilLazy over maximum`() {
        val parser = untilLazy(letter, char(','), max = 2)
        shouldThrowMessage<ParseException>("Stop not found") {
            parser.parse("abc,123,abc")
        }
    }

    @Test
    fun `sepByGreedy`() {
        val parser = sepByGreedy(letter, char(','))
        parser.parse("a,b,c") shouldBe listOf("a", "b", "c")
    }

    @Test
    fun `sepByGreedy fail after separator`() {
        val parser = sepByGreedy(letter, char(',')) and char(',')
        parser.parse("a,b,c,1") shouldBe (listOf("a", "b", "c") to ",")
    }

    @Test
    fun `sepByGreedy empty`() {
        val parser = sepByGreedy(letter, char(','))
        shouldThrowMessage<ParseException>("Combinator failed, parser number 1 with error: Character 1 is not a letter") {
            parser.parse("1,2,3") shouldBe emptyList<String>()
        }
    }

    @Test
    fun `sepByGreedyAllowEmpty`() {
        val parser = sepByGreedyAllowEmpty(letter, char(','))
        parser.parse("a,b,c") shouldBe listOf("a", "b", "c")
    }

    @Test
    fun `sepByGreedyAllowEmpty fail after separator`() {
        val parser = sepByGreedyAllowEmpty(letter, char(',')) and char(',')
        parser.parse("a,b,c,1") shouldBe (listOf("a", "b", "c") to ",")
    }

    @Test
    fun `sepByGreedyAllowEmpty empty`() {
        val parser = sepByGreedyAllowEmpty(letter, char(','))
        parser.parse("1,2,3") shouldBe emptyList<String>()
    }

}

