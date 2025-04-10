package nl.w8mr.parsek

import io.kotest.matchers.shouldBe
import nl.w8mr.parsek.test.shouldThrowMessage
import nl.w8mr.parsek.text.char
import nl.w8mr.parsek.text.parse
import kotlin.js.JsName
import kotlin.test.Test

class RefTest {
    @Test
    @JsName("SimpleRef")
    fun `simple ref`() {
        val holder = object {
            val c: Parser<Char, Pair<String, String>> = seq(ref(::a), ref(::b))
            val a = char('a')
            val b = char('b')

        }
        holder.c.parse("abc") shouldBe ("a" to "b")
    }

    @Test
    @JsName("RecursiveRef")
    fun `recursive ref`() {
        data class Group(val subGroups: List<Group>)
        val holder = object {
            val group: Parser<Char, Group> = seq(char('('), zeroOrMore(ref(::group)), char(')')) { _, g, _ -> Group(g) }
        }
        holder.group.parse("()") shouldBe Group(emptyList())
        holder.group.parse("(())") shouldBe Group(listOf(Group(emptyList())))
        holder.group.parse("(()())") shouldBe Group(listOf(Group(emptyList()),Group(emptyList())))
        holder.group.parse("(()(()))") shouldBe Group(listOf(Group(emptyList()),Group(listOf(Group(emptyList())))))
        shouldThrowMessage<ParseException>("Combinator failed, parser number 3 with error: Character null does not meet expected )") {
            holder.group.parse("(()(())")
        }


    }

}