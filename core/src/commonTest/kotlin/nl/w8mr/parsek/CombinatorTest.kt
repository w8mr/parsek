package nl.w8mr.parsek

import nl.w8mr.parsek.text.*

import kotlin.test.Test
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrowMessage
import kotlin.js.JsName


class CombinatorTest {
    @Test
    @JsName("combinatorShouldCombineTwoParsers")
    fun `combinator should combine two parsers`() {
        val parser = combi {
            val a = -digit
            -char('-')
            val b = -letter
            "$a $b"
        }
        parser.parse("6-a") shouldBe "6 a"
    }

    @Test
    @JsName("combinatorShouldFailWhenFirstParserFails")
    fun `combinator should fail when first parser fails`() {
        val parser = combi {
            val a = -digit
            -char('-')
            val b = -letter
            "$a $b"
        }
        shouldThrowMessage("Combinator failed, parser number 1 with error: Character a is not a digit") {
            parser.parse("a-a")
        }
    }

    @Test
    @JsName("combinatorShouldFailWhenThirdParserFails")
    fun `combinator should fail when third parser fails`() {
        val parser = combi {
            val a = -digit
            -char('-')
            val b = -letter
            "$a $b"
        }
        shouldThrowMessage("Combinator failed, parser number 3 with error: Character 6 is not a letter") {
            parser.parse("6-6")
        }
    }

    @Test
    @JsName("literalCombinator")
    fun `literal combinator`() {
        val parser: LiteralParser<Char> = literalCombi {
            -digit
        }
        parser.parse("1") shouldBe Unit
    }

    @Test
    @JsName("literalCombinatorFails")
    fun `literal combinator fails`() {
        val parser: LiteralParser<Char> = literalCombi {
            -digit
        }
        shouldThrowMessage("Combinator failed, parser number 1 with error: Character a is not a digit") {
            parser.parse("a")
        }
    }

}
