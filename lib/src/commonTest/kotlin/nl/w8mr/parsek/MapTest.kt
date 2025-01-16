package nl.w8mr.parsek

import nl.w8mr.parsek.text.parse
import nl.w8mr.parsek.text.letter
import nl.w8mr.parsek.text.number
import nl.w8mr.parsek.text.some

import kotlin.test.Test
import io.kotest.matchers.shouldBe
import nl.w8mr.parsek.test.shouldThrowMessage

class MapTest {
    @Test
    fun `map to object`() {
        data class Container(val text: String)

        val parser = some(letter).map { Container(it) }
        parser.parse("abc123") shouldBe Container("abc")
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