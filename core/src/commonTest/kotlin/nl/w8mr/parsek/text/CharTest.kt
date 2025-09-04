package nl.w8mr.parsek.text

import nl.w8mr.parsek.ParseException

import nl.w8mr.parsek.test.shouldThrowMessage
import io.kotest.matchers.shouldBe
import kotlin.js.JsName
import kotlin.test.Test

class CharTest {
    @Test
    @JsName("CharMatchesFixedChar")
    fun `char matches fixed char`() {
// ZIPDOK start char-fixed
        val parser = char('a')
        parser.parse("ab") shouldBe "a"
// ZIPDOK end char-fixed
    }

    @Test
    @JsName("CharDoesntMatchFixedChar")
    fun `char doesn't match fixed char`() {
        val parser = char('b')
        shouldThrowMessage<ParseException>("Character a does not meet expected b") {
            parser.parse("ab")
        }
    }

    @Test
    @JsName("CharMatchesAnyCharacter")
    fun `char matches any character`() {
// ZIPDOK start any-char-literal
        val parser = anyChar
        parser.parse("★☆") shouldBe "★"
// ZIPDOK end any-char-literal
    }

    @Test
    @JsName("CharDoesntMatchAnyCharacter")
    fun `char doesn't match any character`() {
        val parser = anyChar
        shouldThrowMessage<ParseException>("No more tokens available") {
            parser.parse("")
        }
    }

    @Test
    @JsName("CharMathcesPredicate")
    fun `char mathces predicate`() {
        val parser = char { it.isLetter() }
        parser.parse("ab") shouldBe "a"
    }

    @Test
    @JsName("CharDoesntMatchPredicate")
    fun `char doesn't match predicate`() {
        val parser = char { it.isLetter()}
        shouldThrowMessage<ParseException>("Character 1 does not meet predicate") {
            parser.parse("12")
        }
    }

    @Test
    @JsName("CharMathcesLetter")
    fun `char mathces letter`() {
        val parser = letter
        parser.parse("ab") shouldBe "a"
    }

    @Test
    @JsName("CharDoesntMatchLetter")
    fun `char doesn't match letter`() {
        val parser = letter
        shouldThrowMessage<ParseException>("Character 1 is not a letter") {
            parser.parse("12")
        }
    }

    @Test
    @JsName("CharMathcesDigit")
    fun `char mathces digit`() {
        val parser = digit
        parser.parse("12") shouldBe "1"
    }

    @Test
    @JsName("CharDoesntMatchDigit")
    fun `char doesn't match digit`() {
        val parser = digit
        shouldThrowMessage<ParseException>("Character a is not a digit") {
            parser.parse("ab")
        }
    }

    @Test
    @JsName("MatchesNumber")
    fun `matches number`() {
        val parser = number
        parser.parse("12ab") shouldBe 12
    }

}
