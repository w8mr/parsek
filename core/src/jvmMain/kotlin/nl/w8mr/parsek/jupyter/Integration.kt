package nl.w8mr.parsek.jupyter

import nl.w8mr.parsek.Parser
import org.jetbrains.kotlinx.jupyter.api.MimeTypes
import org.jetbrains.kotlinx.jupyter.api.createRenderer
import org.jetbrains.kotlinx.jupyter.api.libraries.JupyterIntegration
import org.jetbrains.kotlinx.jupyter.api.mimeResult

class Integration: JupyterIntegration() {
    override fun Builder.onLoaded() {
        addRenderer(createRenderer<Parser.Result<*>> { result ->
            mimeResult(
                MimeTypes.HTML to renderResult(result),
                MimeTypes.PLAIN_TEXT to ((result as? Parser.Success)?.value.toString() ?: "Failure: ${(result as? Parser.Failure)?.error}")
            )

        })
    }
}
