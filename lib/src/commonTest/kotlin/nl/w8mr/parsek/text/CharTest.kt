package nl.w8mr.parsek.text

import nl.w8mr.parsek.ParseException
import nl.w8mr.parsek.repeat
import nl.w8mr.parsek.text.*

import nl.w8mr.parsek.test.shouldThrowMessage
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class CharTest {
    @Test
    fun `char matches literal`() {
// ZIPDOK start char-literal
        val parser = char('a')
        parser.parse("ab") shouldBe 'a'
// ZIPDOK end char-literal
    }

    @Test
    fun `char doesn't match literal`() {
        val parser = char('b')
        shouldThrowMessage<ParseException>("Character a does not meet expected b") {
            parser.parse("ab")
        }
    }

    @Test
    fun `char matches any character`() {
// ZIPDOK start any-char-literal
        val parser = char
        parser.parse("★☆") shouldBe '★'
// ZIPDOK end any-char-literal
    }

    @Test
    fun `char doesn't match any character`() {
        val parser = char
        shouldThrowMessage<ParseException>("{EoF} found, not a regular character") {
            parser.parse("")
        }
    }

    @Test
    fun `char mathces predicate`() {
        val parser = char { it.isLetter() }
        parser.parse("ab") shouldBe 'a'
    }

    @Test
    fun `char doesn't match predicate`() {
        val parser = char { it.isLetter()}
        shouldThrowMessage<ParseException>("Character 1 does not meet predicate") {
            parser.parse("12")
        }
    }

    @Test
    fun `char mathces letter`() {
        val parser = letter
        parser.parse("ab") shouldBe 'a'
    }

    @Test
    fun `char doesn't match letter`() {
        val parser = letter
        shouldThrowMessage<ParseException>("Character 1 is not a letter") {
            parser.parse("12")
        }
    }

    @Test
    fun `char mathces digit`() {
        val parser = digit
        parser.parse("12") shouldBe '1'
    }

    @Test
    fun `char doesn't match digit`() {
        val parser = digit
        shouldThrowMessage<ParseException>("Character a is not a digit") {
            parser.parse("ab")
        }
    }

    @Test
    fun `chars asString`() {
        val parser = repeat(letter).asString
        parser.parse("abc123") shouldBe "abc"

    }

    @Test
    fun `matches number`() {
        val parser = number
        parser.parse("12ab") shouldBe 12
    }

}
