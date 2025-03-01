import io.kotest.matchers.shouldBe
import nl.w8mr.parsek.text.digit
import nl.w8mr.parsek.text.letter
import nl.w8mr.parsek.text.parse
import kotlin.test.Test

class OneOfTest {
    @Test
    fun `textParser or textParser`() {
        val parser = letter or digit
        parser.parse("1") shouldBe "1"
    }

    @Test
    fun `char or textParser`() {
        val parser = 'a' or digit
        parser.parse("1") shouldBe "1"
    }

    @Test
    fun `textParser or char`() {
        val parser = letter or '1'
        parser.parse("1") shouldBe "1"
    }

    @Test
    fun `char or char`() {
        val parser = 'a' or '1'
        parser.parse("1") shouldBe "1"
    }

    @Test
    fun `char or char or char`() {
        val parser = 'a' or 'b' or '1'
        parser.parse("1") shouldBe "1"
    }

}
