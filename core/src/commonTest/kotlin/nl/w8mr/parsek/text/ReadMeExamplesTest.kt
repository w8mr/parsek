package nl.w8mr.parsek.text

import io.kotest.matchers.shouldBe
import nl.w8mr.parsek.ParseException
import nl.w8mr.parsek.Parser
import nl.w8mr.parsek.and
import nl.w8mr.parsek.combi
import nl.w8mr.parsek.map
import nl.w8mr.parsek.or
import nl.w8mr.parsek.ref
import nl.w8mr.parsek.sepBy
import nl.w8mr.parsek.test.shouldThrowMessage
import kotlin.test.Test

class ReadMeExamplesTest {

    @Test
    fun `test parsing a comma-separated list of numbers`() {
        val digit = char { it.isDigit() }
        val number = repeat(digit, min = 1) map { it.toInt() }
        val comma = char(',')
        val numberList = number sepBy comma

        numberList("123,45,6") shouldBe listOf(123, 45, 6)
    }

    @Test
    fun `test parsing nested structures`() {
        val container = object {

            val openBracket = literal('[')
            val closeBracket = literal(']')
            val comma = char(',')
            val number = repeat(char { it.isDigit() }, min = 1) map { it.toInt() }
            val value = ref(::list) or number
            val list: Parser<Char, List<Any>> = openBracket and (value sepBy comma) and closeBracket
        }
        container.list("[1,[2,3],4]") shouldBe
                listOf(1, listOf(2, 3), 4)
    }

    @Test
    fun `test key-value pair parsing`() {
        val keyValueParser = combi<Char, Pair<String, String>> {
            val key = repeat(char { it.isLetterOrDigit() }).bind()
            -char('=')
            val value = repeat(char { it.isLetterOrDigit() || it == ' ' }, min = 1).bind()

            key to value
        }

        keyValueParser("username=John Doe") shouldBe
                ("username" to "John Doe")
    }

    @Test
    fun `test custom error handling`() {
        val safeKeyValueParser = combi {
            val key = repeat(char { it.isLetterOrDigit() }).bind()
            if (key.isEmpty()) {
                fail("Key cannot be empty")
            }

            -char('=')

            val value = repeat(char { it.isLetterOrDigit() || it == ' ' }, min = 1).bind()
            if (value.isEmpty()) {
                fail("Value cannot be empty")
            }

            key to value
        }

        shouldThrowMessage<ParseException>("Combinator failed, parser number 1 with error: Key cannot be empty") {
            safeKeyValueParser("=John Doe") shouldBe
                    ("" to "John Doe")
        }
    }

    @Test
    fun `test comb with bindResult`() {
        val repeatedParser = combi {
            val list = mutableListOf<String>()
            val parser = char { it.isLetter() }

            while (list.size < 3) { // Ensure at least 3 elements are parsed
                when (val result = parser.bindAsResult()) {
                    is Parser.Success -> list.add(result.bind())
                    is Parser.Failure -> fail("Only ${list.size} elements found, needed at least 3")
                }
            }

            while (list.size < 5) { // Parse up to 5 elements
                when (val result = parser.bindAsResult()) {
                    is Parser.Success -> list.add(result.bind())
                    is Parser.Failure -> break
                }
            }

            list
        }

        repeatedParser("abcde") shouldBe listOf("a", "b", "c", "d", "e")

        shouldThrowMessage<ParseException>("Combinator failed, parser number 3 with error: Only 2 elements found, needed at least 3") {
            repeatedParser("ab") shouldBe listOf("a", "b")
        }
    }

}