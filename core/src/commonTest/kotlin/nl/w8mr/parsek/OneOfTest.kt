package nl.w8mr.parsek

import io.kotest.matchers.shouldBe
import nl.w8mr.parsek.test.shouldThrowMessage
import nl.w8mr.parsek.text.*
import nl.w8mr.parsek.text.some
import kotlin.test.Test

class OneOfTest {
    @Test
    fun `oneOf first match`() {
        val parser = oneOf(some(digit), some(letter))
        parser.parse("123abc") shouldBe "123"
    }

    @Test
    fun `oneOf second match`() {
        val parser = oneOf(some(digit), some(letter))
        parser.parse("abc123") shouldBe "abc"
    }

    @Test
    fun `oneOf no match`() {
        val parser = oneOf(some(digit), some(letter))
        shouldThrowMessage<ParseException>("None of the parsers matches") {
            parser.parse("★☆abc123")
        }
    }

    @Test
    fun `oneOf no match tree`() {
        val parser = oneOf(some(digit), some(letter))
        val full = parser.parseTree("★☆abc123")
        full.first shouldBe null
        full.second shouldBe Parser.Failure("None of the parsers matches",
            listOf(
                Parser.Failure<List<Char>>("Repeat only 0 elements found, needed at least 1",
                    listOf(Parser.Failure<Char>("Character ★ is not a digit"))),
                Parser.Failure<List<Char>>("Repeat only 0 elements found, needed at least 1",
                    listOf(Parser.Failure<Char>("Character ★ is not a letter"))),
            )
        )
    }

    @Test
    fun `oneOf first match tree`() {
        val parser = oneOf(some(digit), some(letter))
        val full = parser.parseTree("123abc")
        full.first shouldBe "123"
        full.second shouldBe Parser.Success("123",
            listOf(
                Parser.Success<String>("123", listOf(
                    Parser.Success("1"),
                    Parser.Success("2"),
                    Parser.Success("3"),
                    Parser.Failure("Character a is not a digit"))
                )
            )
        )
    }

    @Test
    fun `oneOf second match tree`() {
        val parser = oneOf(some(digit), some(letter))
        val full = parser.parseTree("abc123")
        full.first shouldBe "abc"
        full.second shouldBe Parser.Success("abc",
            listOf(
                Parser.Failure<String>("Repeat only 0 elements found, needed at least 1", listOf(
                    Parser.Failure<Char>("Character a is not a digit"))),
                Parser.Success<String>("abc", listOf(
                    Parser.Success("a"),
                    Parser.Success("b"),
                    Parser.Success("c"),
                    Parser.Failure("Character 1 is not a letter")))
            )
        )
    }

    @Test
    fun `or first match`() {
        val parser = some(digit) or some(letter)
        parser.parse("123abc") shouldBe "123"
    }

    @Test
    fun `or second match`() {
        val parser = some(digit) or some(letter)
        parser.parse("abc123") shouldBe "abc"
    }

    @Test
    fun `or third match`() {
        val parser = char('a') or char('b') or char('c')
        parser.parse("cba") shouldBe "c"
    }

    @Test
    fun `liter or match`() {
        val parser = literal('a') or literal('b') or literal('c')
        parser.parse("cba") shouldBe Unit
    }

}