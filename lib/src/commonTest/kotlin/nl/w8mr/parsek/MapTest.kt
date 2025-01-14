package nl.w8mr.parsek

import nl.w8mr.parsek.text.parse
import nl.w8mr.parsek.text.letter
import nl.w8mr.parsek.text.number

import kotlin.test.Test
import io.kotest.matchers.shouldBe
import nl.w8mr.parsek.test.shouldThrowMessage

class MapTest {
    @Test
    fun `map to object`() {
        data class Container(val list: List<Char>)

        val parser = some(letter).map { Container(it) }
        parser.parse("abc123") shouldBe Container(listOf('a','b','c'))
    }

    @Test
    fun `filter`() {
        val parser = number.filter { it < 25 }
        parser.parse("12abc") shouldBe 12
        shouldThrowMessage<ParseException>("Predicate not met") {
            parser.parse("123abc")
        }
    }

}