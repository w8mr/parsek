package nl.w8mr.parsek

fun eof() = object: Parser<Unit>() {
    override fun apply(context: Context): Result<Unit> =
        when  {
            !context.hasNext() -> context.success(Unit, 0 )
            else -> context.error("End of file not found")
        }
}