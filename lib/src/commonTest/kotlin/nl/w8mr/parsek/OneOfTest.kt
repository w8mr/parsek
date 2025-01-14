package nl.w8mr.parsek

import io.kotest.matchers.shouldBe
import nl.w8mr.parsek.test.shouldThrowMessage
import nl.w8mr.parsek.text.*
import kotlin.test.Test

class OneOfTest {
    @Test
    fun `oneOf first match`() {
        val parser = oneOf(some(digit), some(letter)).asString
        parser.parse("123abc") shouldBe "123"
    }

    @Test
    fun `oneOf second match`() {
        val parser = oneOf(some(digit), some(letter)).asString
        parser.parse("abc123") shouldBe "abc"
    }

    @Test
    fun `oneOf no match`() {
        val parser = oneOf(some(digit), some(letter)).asString
        shouldThrowMessage<ParseException>("None of the parsers matches") {
            parser.parse("★☆abc123")
        }
    }

    @Test
    fun `oneOf second match tree`() {
        val parser = oneOf(some(digit), some(letter))
        val full = parser.parseTree("★☆abc123")
        full.first shouldBe null
        full.second shouldBe Parser.Error("None of the parsers matches",
            listOf(
                Parser.Error<List<Char>>("Repeat only 0 elements found, needed at least 1",
                    listOf(Parser.Error<Char>("Character ★ is not a digit"))),
                Parser.Error<List<Char>>("Repeat only 0 elements found, needed at least 1",
                    listOf(Parser.Error<Char>("Character ★ is not a letter"))),
            )
        )
    }

    @Test
    fun `oneOf no match tree`() {
        val parser = oneOf(some(digit), some(letter))
        val full = parser.parseTree("abc123")
        full.first shouldBe listOf('a', 'b', 'c')
        full.second.subResults.size shouldBe 2
        full.second.subResults[0] shouldBe Parser.Error<Char>("Repeat only 0 elements found, needed at least 1", listOf(
            Parser.Error<Char>("Character a is not a digit")))
        full.second.subResults[1] shouldBe Parser.Success<List<Char>>(
            listOf('a', 'b', 'c'), listOf(
                Parser.Success('a'),
                Parser.Success('b'),
                Parser.Success('c'),
                Parser.Error("Character 1 is not a letter")))
    }

}