package nl.w8mr.parsek

import io.kotest.matchers.shouldBe
import nl.w8mr.parsek.test.shouldThrowMessage
import nl.w8mr.parsek.text.CharSequenceContext
import nl.w8mr.parsek.text.literal
import kotlin.js.JsName
import kotlin.test.Test

class LiteralTest {
    @Test
    @JsName("charLiteral")
    fun `char literal`() {
        val parser = literal('a')
        parser(CharSequenceContext("abc")) shouldBe Unit
    }

    @Test
    @JsName("stringLiteral")
    fun `string literal`() {
        val parser = literal("abc")
        parser(CharSequenceContext("abcdef")) shouldBe Unit
    }

    @Test
    @JsName("stringLiteralEoT")
    fun `string literal EoT`() {
        val parser = literal("abc")
        shouldThrowMessage<ParseException>("No more tokens available") {
            parser(CharSequenceContext("ab")) shouldBe Unit
        }
    }

    @Test
    @JsName("stringLiteralPartial")
    fun `string literal partial`() {
        val parser = literal("abc")
        shouldThrowMessage<ParseException>("Character d does not meet expected c, partial match: ab") {
            parser(CharSequenceContext("abdef")) shouldBe Unit
        }
    }

    sealed interface Suit
    object CLUBS: Suit
    object DIAMONDS: Suit
    object HEARTS: Suit
    object SPADES: Suit

    @Test
    @JsName("token")
    fun `token`() {
        val parser = token<Suit, CLUBS>(CLUBS::class)
        parser.invoke(listOf(CLUBS, DIAMONDS, HEARTS, SPADES)) shouldBe CLUBS
    }

    @Test
    @JsName("tokenFail")
    fun `token  fail`() {
        val parser = token<Suit, DIAMONDS>(DIAMONDS::class)
        shouldThrowMessage<ParseException>("token is not instance of DIAMONDS") {
            parser.invoke(listOf(CLUBS, DIAMONDS, HEARTS, SPADES)) shouldBe Unit
        }
    }

    @Test
    @JsName("tokenLiteral")
    fun `token literal`() {
        val parser = literal<Suit, CLUBS>(CLUBS::class)
        parser.invoke(listOf(CLUBS, DIAMONDS, HEARTS, SPADES)) shouldBe Unit
    }

    @Test
    @JsName("tokenLiteralFail")
    fun `token literal fail`() {
        val parser = literal<Suit, DIAMONDS>(DIAMONDS::class)
        shouldThrowMessage<ParseException>("token is not instance of DIAMONDS") {
            parser.invoke(listOf(CLUBS, DIAMONDS, HEARTS, SPADES)) shouldBe Unit
        }
    }



}