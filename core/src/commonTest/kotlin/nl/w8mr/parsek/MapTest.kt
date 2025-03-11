package nl.w8mr.parsek

import nl.w8mr.parsek.text.parse
import nl.w8mr.parsek.text.letter
import nl.w8mr.parsek.text.number
import nl.w8mr.parsek.text.some

import kotlin.test.Test
import io.kotest.matchers.shouldBe
import nl.w8mr.parsek.test.shouldThrowMessage
import nl.w8mr.parsek.text.literal

class MapTest {
    @Test
    fun `map to object`() {
        data class Container(val text: String)

        val parser = some(letter).map { Container(it) }
        parser.parse("abc123") shouldBe Container("abc")
    }

    @Test
    fun `filter values`() {
        val parser = number filter { it < 25 }
        parser.parse("12abc") shouldBe 12
    }

    @Test
    fun `filter values fails`() {
        val parser = number filter { it < 25 }
        shouldThrowMessage<ParseException>("Predicate not met") {
            parser.parse("123abc")
        }
    }


    @Test
    fun `filter values fails with message`() {
        val parser = number.filter("Number not below 25") { it < 25 }
        shouldThrowMessage<ParseException>("Number not below 25") {
            parser.parse("123abc")
        }
    }

    @Test
    fun `asLiteral`() {
        val parser: LiteralParser<Char> = number.asLiteral()
        parser.parse("12abc") shouldBe Unit
    }

    @Test
    fun `literal as value`() {
        val parser = literal("true") value true
        parser.parse("true 12") shouldBe true
    }


}