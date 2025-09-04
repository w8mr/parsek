package nl.w8mr.parsek

import kotlin.reflect.KProperty0

fun <Token, R> ref(parserRef: KProperty0<Parser<Token, R>>): Parser<Token, R> =
    combi {
        val index = index()
        val refs: MutableSet<Pair<KProperty0<Parser<Token, R>>, Long>> = stateGetOrPut("refs") { mutableSetOf<Pair<KProperty0<Parser<Token, R>>, Long>>() }
                as? MutableSet<Pair<KProperty0<Parser<Token, R>>, Long>> ?: error("refs should be set of Pair property int")
        if ((parserRef to index) in refs) {
            fail("Recursion detected")
        } else {
            refs.add(parserRef to index)
            parserRef.get().bind().also {
                refs.remove(parserRef to index)
            }
        }
    }
