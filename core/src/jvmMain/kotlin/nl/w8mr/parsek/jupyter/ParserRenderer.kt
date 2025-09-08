package nl.w8mr.parsek.jupyter

import nl.w8mr.parsek.Parser

// Jupyter notebook renderer for Parser.Result
@Suppress("unused")
@JvmName("ParsekNotebookRenderers")

fun renderResult(result: Parser.Result<*>): String {
    fun renderSubresults(subresults: List<Parser.Result<*>>): String {
        if (subresults.isEmpty()) return ""
        return """
            <details style="margin-left:1em" closed>
                <summary>Subresults (${subresults.size})</summary>
                <ul>
                    ${subresults.joinToString("") { "<li>${renderResult(it)}</li>" }}
                </ul>
            </details>
        """.trimIndent()
    }
    return when (result) {
        is Parser.Success<*> -> """
            <div>
                <b>Success:</b> ${result.value}
                ${renderSubresults(result.subResults)}
            </div>
        """.trimIndent()
        is Parser.Failure -> """
            <div>
                <b>Failure:</b> ${result.error}
                ${renderSubresults(result.subResults)}
            </div>
        """.trimIndent()
    }
}

// Register renderer for Jupyter
//val parsekResultRenderer: Renderer<Parser.Result<*>> = { result ->
//    DisplayResult.HTML(renderResult(result))
//}

//val renderers = renderers {
//    register<Parser.Result<*>>(parsekResultRenderer)
//}
