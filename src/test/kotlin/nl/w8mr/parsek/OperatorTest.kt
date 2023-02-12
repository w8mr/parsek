package nl.w8mr.parsek

import nl.w8mr.parsek.text.Parsers.followedBy
import nl.w8mr.parsek.text.char
import nl.w8mr.parsek.text.regex
import nl.w8mr.parsek.text.Parsers.number
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class OperatorTest {
    @Test
    fun array() {
        open class T()
        data class A(val size: Int, val value: T): T()
        data class N(val name: String): T()

        val name: Parser<T> = regex("[a-z]*").map(::N)
        val arraySubscript = ('[' followedBy number followedBy ']').unary<Int, T> { s -> { acc -> A(s, acc) } }
        val array = name.postfix(arraySubscript)
        val test = array.parse("test[2][3][4]")

        check(test is A)
        assertEquals(test.size, 4)
        check(test.value is A)
        assertEquals(test.value.size, 3)
        check(test.value.value is A)
        assertEquals(test.value.value.size, 2)
        check(test.value.value.value is N)
        assertEquals(test.value.value.value.name, "test")
    }

    @Test
    fun numberPostfix() {
        val digit = char { it in '0'..'9' }.map { it - '0' }
        val nmbr = digit.postfix(digit.unary { r -> { acc -> acc * 10 + r }})
        val test = nmbr.parse("512")

        assertEquals(512, test)
    }

    fun pow(b: Int, p: Int): Int = when {
        p == 0 -> 1
        else -> b * pow(b,p-1)
    }

    @Test
    fun power() {
        val expr = number.infixr(char('^').binary { ::pow })
        val test = expr.parse("4^3^2")
        assertEquals(262144, test)
    }

    @Test
    fun plusminus() {
        val plus = char('+').binary { Int::plus }
        val minus = char('-').binary { Int::minus }
        val expr = number.infixl(plus or minus)
        val test = expr.parse("4-3+2-1")
        assertEquals(2, test)
    }

    @Test
    fun calc() {
        val pw = char('^').binary { ::pow }
        val times = char('*').binary { Int::times }
        val div = char('/').binary { Int::div }
        val plus = char('+').binary { Int::plus }
        val minus = char('-').binary { Int::minus }
        val neg = char('-').unary<Char, Int> { { n -> -n } }
        val positveNumber = regex("?\\d+") map { it.toInt() }
        val expr4 = positveNumber.prefix(neg)
        val expr3 = expr4.infixr(pw)
        val expr2 = expr3.infixl(times or div)
        val expr1 = expr2.infixl(minus or plus)
        val test = expr1.parse("-1+2*3^2+-4")
        assertEquals(13, test)
    }

    @Test
    fun numberEmptyOperator() {
        val digit = char { it in '0'..'9' }.map { it - '0' }
        val nmbr = digit.infixl(empty().binary { _ -> { acc, r -> acc * 10 + r }})
        val test = nmbr.parse("512")

        assertEquals(512, test)
    }

    @Test
    fun negate() {
        val expr = number.prefix(char('-').unary { _ -> { n -> -n } })
        val test = expr.parse("-------42")
        assertEquals(-42, test)

    }


    @Test
    fun calcWithTable() {

        val positveNumber = regex("?\\d+") map { it.toInt() }
        val table = OparatorTable.create(positveNumber) {
            prefix(40, char('-').unary { { n -> -n } })
            infixr(30, char('^').binary { ::pow })
            infixl(20, char('*').binary { Int::times })
            infixl(20, char('/').binary { Int::div })
            infixl(10, char('+').binary { Int::plus })
            infixl(10, char('-').binary { Int::minus })
        }

        val test = table.parse("-1+2*3^2+-4")
        assertEquals(13, test)
    }

}

