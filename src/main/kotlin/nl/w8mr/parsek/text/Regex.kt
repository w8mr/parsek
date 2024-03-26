package nl.w8mr.parsek.text

import nl.w8mr.parsek.Context
import nl.w8mr.parsek.Parser


fun regex(pattern: String) = object :  Parser<String>() {
    override fun apply(context: Context): Result<String> {
        check(context.source is CharSequence) //TODO: Check how to handle better
        val regex = "^$pattern".toRegex()
        val input = context.source.subSequence(context.index, context.source.length)
        return when (val result = regex.find(input)) {
            null -> when (context.errorLevel) {
                3 -> {
                    val cpattern = regex.toPattern()
                    var i = 0
                    while (true) {
                        val m = cpattern.matcher(input.subSequence(0, i))
                        m.find()
                        if (!m.hitEnd()) break
                        i++
                    }
                    i--
                    when (i) {
                        0 -> context.error("Expected pattern $pattern to match")
                        else -> context.error(
                            "Partial match for pattern $pattern, ${input.subSequence(0, i)
                            } matched, failed for character ${input[i]}", length = i
                        )
                    }
                }

                else -> context.error("Expected pattern $pattern to match", 0)
            }
            else -> return context.success(result.value, result.value.length)
        }
    }
}
//partial match for patttern regex, reg matched, no match for character u