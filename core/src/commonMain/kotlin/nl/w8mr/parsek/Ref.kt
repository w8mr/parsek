package nl.w8mr.parsek

import kotlin.reflect.KProperty0

fun <Token, R> ref(parserRef: KProperty0<Parser<Token, R>>): Parser<Token, R> =
    combi {
        val mark = mark()
        val refs: MutableSet<Pair<KProperty0<Parser<Token, R>>, Int>> = state.getOrPut("refs") { mutableSetOf<Pair<KProperty0<Parser<Token, R>>, Int>>() }
                as? MutableSet<Pair<KProperty0<Parser<Token, R>>, Int>> ?: error("refs should be set of Pair property int")
        if ((parserRef to mark) in refs) {
            fail("Recursion detected")
        } else {
            refs.add(parserRef to mark)
            parserRef.get().bind().also {
                refs.remove(parserRef to mark)
            }
        }
    }
