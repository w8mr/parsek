import io.kotest.matchers.shouldBe
import nl.w8mr.parsek.*
import kotlin.test.Test

import nl.w8mr.parsek.text.*
import nl.w8mr.parsek.text.invoke
import nl.w8mr.parsek.test.shouldThrowMessage
import kotlin.js.JsName

class RepetitionTest {
    @Test
    @JsName("RepeatLetter")
    fun `repeat letter`() {
        val parser = repeat(letter)
        parser("abc123") shouldBe "abc"
    }

    @Test
    @JsName("RepeatWithMaximum")
    fun `repeat with maximum`() {
        val parser = repeat(letter, 2)
        parser("abc123") shouldBe "ab"
    }

    @Test
    @JsName("RepeatWithMinimum")
    fun `repeat with minimum`() {
        val parser = repeat(letter, min = 2)
        parser("abc123") shouldBe "abc"
    }

    @Test
    @JsName("RepeatWithMinimumFailing")
    fun `repeat with minimum failing`() {
        val parser = repeat(letter, min = 4)
        shouldThrowMessage<ParseException>("Repeat only 3 elements found, needed at least 4") {
            parser("abc123")

        }
    }

    @Test
    @JsName("SomeLetters")
    fun `some letters`() {
        val parser = some(letter)
        parser("abc123") shouldBe "abc"
    }

    @Test
    @JsName("SomeLettersTree")
    fun `some letters tree`() {
        val parser = some(letter)
        val full = parser.parse("abc123")
        full.first shouldBe "abc"
        full.second shouldBe Parser.Success("abc", listOf(
            Parser.Success("a"),
            Parser.Success("b"),
            Parser.Success("c"),
            Parser.Failure("Character 1 is not a letter")
        ))
    }

    @Test
    @JsName("SomeLettersAsStringTree")
    fun `some letters asString tree`() {
        val parser = some(letter)
        val full = parser.parse("abc123")
        full.first shouldBe "abc"
        full.second shouldBe Parser.Success("abc", listOf(
            Parser.Success("a"),
            Parser.Success("b"),
            Parser.Success("c"),
            Parser.Failure("Character 1 is not a letter"),
        ))
    }

    @Test
    @JsName("SomeLettersFailing")
    fun `some letters failing`() {
        val parser = some(letter)
        shouldThrowMessage<ParseException>("Repeat only 0 elements found, needed at least 1") {
            parser("123")
        }
    }

    @Test
    @JsName("ZeroOrMoreEmpty")
    fun `zeroOrMore empty`() {
        val parser = zeroOrMore(letter)
        parser("123") shouldBe ""
    }

    @Test
    @JsName("TimesPrefix")
    fun `times prefix`() {
        val parser = (4*letter)
        parser("abcdef") shouldBe "abcd"
    }

    @Test
    @JsName("TimesPostfix")
    fun `times postfix`() {
        val parser = (digit*4)
        parser("123456") shouldBe "1234"
    }

    @Test
    @JsName("TimesRange")
    fun `times range`() {
        val parser = ((2..4)*letter)
        parser("abcdef") shouldBe "abcd"
        shouldThrowMessage<ParseException>("Repeat only 1 elements found, needed at least 2") {
            parser("a12345")

        }
    }

    @Test
    @JsName("SomeCombination")
    fun `some combination`() {
        val parser = combi {
            val letters = -some(letter)
            val digits = -some(digit)
            letters to digits
        }
        parser("abc123") shouldBe ("abc" to "123")
    }

    @Test
    @JsName("UntilLazy")
    fun `untilLazy`() {
        val parser = untilLazy(letter, char(','))
        parser("abc,123,abc") shouldBe ("abc" to ",")
    }

    @Test
    @JsName("UntilLazyChar")
    fun `untilLazyChar`() {
        val parser = letter until ','
        parser("abc,123,abc") shouldBe "abc"
    }

    @Test
    @JsName("UntilLazyFailing")
    fun `untilLazy failing`() {
        val parser = untilLazy(letter, char(','))
        shouldThrowMessage<ParseException>("Stop not found") {
            parser("abc123,123,abc")
        }
    }


    @Test
    @JsName("UntilLazyLessThenMinimum")
    fun `untilLazy less then minimum`() {
        val parser = untilLazy(letter, char(','), min = 4)
        shouldThrowMessage<ParseException>("Repeat only 3 elements found, needed at least 4") {
            parser("abc,123,abc")
        }
    }

    @Test
    @JsName("UntilLazyOverMinimum")
    fun `untilLazy over minimum`() {
        val parser = untilLazy(letter, char(','), min = 2)
        parser("abc,123,abc") shouldBe ("abc" to ",")

    }

    @Test
    @JsName("UntilLazyOverMaximum")
    fun `untilLazy over maximum`() {
        val parser = untilLazy(letter, char(','), max = 2)
        shouldThrowMessage<ParseException>("Stop not found") {
            parser("abc,123,abc")
        }
    }

    @Test
    @JsName("SepByGreedy")
    fun `sepByGreedy`() {
        val parser = sepByGreedy(letter, char(','))
        parser("a,b,c") shouldBe listOf("a", "b", "c")
    }

    @Test
    @JsName("SepByGreedyFailAfterSeparator")
    fun `sepByGreedy fail after separator`() {
        val parser = sepByGreedy(letter, char(',')) and char(',')
        parser("a,b,c,1") shouldBe (listOf("a", "b", "c") to ",")
    }

    @Test
    @JsName("SepByGreedyEmpty")
    fun `sepByGreedy empty`() {
        val parser = sepByGreedy(letter, char(','))
        shouldThrowMessage<ParseException>("Combinator failed, parser number 1 with error: Character 1 is not a letter") {
            parser("1,2,3") shouldBe emptyList<String>()
        }
    }

    @Test
    @JsName("SepByGreedyAllowEmpty")
    fun `sepByGreedyAllowEmpty`() {
        val parser = sepByGreedyAllowEmpty(letter, char(','))
        parser("a,b,c") shouldBe listOf("a", "b", "c")
    }

    @Test
    @JsName("SepByGreedyAllowEmptyFailAfterSeparator")
    fun `sepByGreedyAllowEmpty fail after separator`() {
        val parser = sepByGreedyAllowEmpty(letter, char(',')) and char(',')
        parser("a,b,c,1") shouldBe (listOf("a", "b", "c") to ",")
    }

    @Test
    @JsName("SepByGreedyAllowEmptyEmpty")
    fun `sepByGreedyAllowEmpty empty`() {
        val parser = sepByGreedyAllowEmpty(letter, char(','))
        parser("1,2,3") shouldBe emptyList<String>()
    }

}
