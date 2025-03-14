import io.kotest.matchers.shouldBe
import nl.w8mr.parsek.text.*
import nl.w8mr.parsek.*
import kotlin.test.Test

class OneOfTest {
    @Test
    fun `textParser or textParser`() {
        val parser = letter or digit
        parser.parse("1") shouldBe "1"
    }

    @Test
    fun `char or textParser`() {
        val parser = char('a') or digit
        parser.parse("1") shouldBe "1"
    }

    @Test
    fun `textParser or char`() {
        val parser = letter or char('1')
        parser.parse("1") shouldBe "1"
    }

    @Test
    fun `char or char`() {
        val parser = 'a' or '1'
        parser.parse("1") shouldBe Unit
    }

    @Test
    fun `char or char or char`() {
        val parser = 'a' or 'b' or '1'
        parser.parse("1") shouldBe Unit
    }

}
