package nl.w8mr.jafun

import nl.w8mr.kasmine.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.PrintStream
import java.util.concurrent.TimeUnit
import kotlin.Array

fun compile(code: String, className: String = "HelloWorld", methodName: String = "main", methodSig: String = "([Ljava/lang/String;)V"): ByteArray {
    val lexed = lexer.parse(code).filter { it !is Token.WS }
    val parsed = Parser.parse(lexed)
    println("PARSED: $parsed")
    println()
    return compile(parsed, className, methodName, methodSig)
}

fun runMain(bytes: ByteArray) {
    writeFile("HelloWorld", bytes)
    runMethod(bytes, "HelloWorld", "main")
}

fun test(code: String, className: String = "HelloWorld", methodName: String = "main", methodSig: String = "([Ljava/lang/String;)V"): String {
    val bytes = compile(code.trimIndent(), className, methodName, methodSig)
    writeFile(className, bytes)
    val oldOut = System.out
    val output = ByteArrayOutputStream()
    System.setOut(PrintStream(output))
    runMethod(bytes, className, methodName)
    System.setOut(oldOut)
    val result = String(output.toByteArray())
    println("OUTPUT: $result")
//    print("DISASSEMBLE: ")
//    println("javap -v HelloWorld.class".runCommand(File("./build/classes/jafun/test")))
    return result

}

private fun runMethod(bytes: ByteArray, className: String, methodName: String): kotlin.Unit {
    val loader = DynamicClassLoader(Thread.currentThread().contextClassLoader)
    val helloWorldClass = loader.define(className, bytes)
    helloWorldClass.getMethod(methodName, Array<String>::class.java).invoke(null, null)
}

private fun writeFile(className: String, bytes: ByteArray) {
    val dir = File("./build/classes/jafun/test")
    dir.mkdirs()
    val file = File(dir, "$className.class")
    file.writeBytes(bytes)
}

fun compile(statements: List<ASTNode>, className: String = "HelloWorld", methodName: String = "main", methodSig: String = "([Ljava/lang/String;)V"  ): ByteArray {
    val clazz = classBuilder {
        name = className
        compileMethod(this, statements, methodName, methodSig)
    }

    return clazz.write()
}
fun compileMethod(classBuilder: ClassBuilder.ClassDSL.DSL, statements: List<ASTNode>, methodName: String, methodSig: String) {
    with(classBuilder) {
        method {
            name = methodName
            signature = methodSig
            statements.forEachIndexed { index, statement -> statement.compile(this, (statements.size - 1) == index) }
            ret()
        }
    }
}

fun String.runCommand(workingDir: File): String? {
    try {
        val parts = this.split("\\s".toRegex())
        val proc = ProcessBuilder(*parts.toTypedArray())
            .directory(workingDir)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()

        proc.waitFor(60, TimeUnit.MINUTES)
        return proc.inputStream.bufferedReader().readText()
    } catch(e: IOException) {
        e.printStackTrace()
        return null
    }
}