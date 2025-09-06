package nl.w8mr.parsek.text

import nl.w8mr.parsek.ParseException

import nl.w8mr.parsek.test.shouldThrowMessage
import io.kotest.matchers.shouldBe
import nl.w8mr.parsek.or
import kotlin.js.JsName
import kotlin.test.Test

class CharTest {
    @Test
    @JsName("CharMatchesFixedChar")
    fun `char matches fixed char`() {
// ZIPDOK start char-fixed
        val parser = char('a')
        parser("ab") shouldBe "a"
// ZIPDOK end char-fixed
    }

    @Test
    @JsName("CharDoesntMatchFixedChar")
    fun `char doesn't match fixed char`() {
        val parser = char('b')
        shouldThrowMessage<ParseException>("Character a does not meet expected b") {
            parser("ab")
        }
    }

    @Test
    @JsName("CharMatchesAnyCharacter")
    fun `char matches any character`() {
// ZIPDOK start any-char-literal
        val parser = anyChar
        parser("★☆") shouldBe "★"
// ZIPDOK end any-char-literal
    }

    @Test
    @JsName("CharDoesntMatchAnyCharacter")
    fun `char doesn't match any character`() {
        val parser = anyChar
        shouldThrowMessage<ParseException>("No more tokens available") {
            parser("")
        }
    }

    @Test
    @JsName("CharMathcesPredicate")
    fun `char mathces predicate`() {
        val parser = char { it.isLetter() }
        parser("ab") shouldBe "a"
    }

    @Test
    @JsName("CharDoesntMatchPredicate")
    fun `char doesn't match predicate`() {
        val parser = char { it.isLetter()}
        shouldThrowMessage<ParseException>("Character 1 does not meet predicate") {
            parser("12")
        }
    }

    @Test
    @JsName("CharMathcesLetter")
    fun `char mathces letter`() {
        val parser = letter
        parser("ab") shouldBe "a"
    }

    @Test
    @JsName("CharDoesntMatchLetter")
    fun `char doesn't match letter`() {
        val parser = letter
        shouldThrowMessage<ParseException>("Character 1 is not a letter") {
            parser("12")
        }
    }

    @Test
    @JsName("CharMathcesDigit")
    fun `char mathces digit`() {
        val parser = digit
        parser("5abc") shouldBe "5"
    }

    @Test
    @JsName("CharDoesntMatchDigit")
    fun `char doesn't match digit`() {
        val parser = digit
        shouldThrowMessage<ParseException>("Character a is not a digit") {
            parser("ab")
        }
    }

    @Test
    @JsName("MatchesNumber")
    fun `matches number`() {
        val parser = number
        parser("123abc") shouldBe 123
    }

    @Test
    @JsName("MatchesIdentifier")
    fun `matches identifier`() {
        val identifier = letter and some(letter or digit)
        identifier("abc123") shouldBe "abc123"
    }

    @Test
    @JsName("MatchesSignedNumber")
    fun `matches signed number`() {
        val parser = signedNumber
        parser("-42") shouldBe -42
        parser("17") shouldBe 17
    }




}
