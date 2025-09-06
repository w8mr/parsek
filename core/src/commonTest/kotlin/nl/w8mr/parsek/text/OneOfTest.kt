import io.kotest.matchers.shouldBe
import nl.w8mr.parsek.text.*
import nl.w8mr.parsek.*
import nl.w8mr.parsek.text.CharSequenceContext
import kotlin.js.JsName
import kotlin.test.Test

class OneOfTest {
    @Test
    @JsName("TextParserOrTextParser")
    fun `textParser or textParser`() {
        val parser = letter or digit
        parser(CharSequenceContext("1")) shouldBe "1"
    }

    @Test
    @JsName("CharOrTextParser")
    fun `char or textParser`() {
        val parser = char('a') or digit
        parser(CharSequenceContext("1")) shouldBe "1"
    }

    @Test
    @JsName("TextParserOrChar")
    fun `textParser or char`() {
        val parser = letter or char('1')
        parser(CharSequenceContext("1")) shouldBe "1"
    }

    @Test
    @JsName("CharOrChar")
    fun `char or char`() {
        val parser = 'a' or '1'
        parser(CharSequenceContext("1")) shouldBe Unit
    }

    @Test
    @JsName("CharOrCharOrChar")
    fun `char or char or char`() {
        val parser = 'a' or 'b' or '1'
        parser(CharSequenceContext("1")) shouldBe Unit
    }

}
