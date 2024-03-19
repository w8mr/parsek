package nl.w8mr.jafun.nl.w8mr.jafun

import nl.w8mr.jafun.test
import kotlin.test.Test
import kotlin.test.assertEquals

class CompilerTest {
    @Test
    fun helloWorldParensCompile() {
        val result = test("""
            println(join("Hello","World"))
        """)
        assertEquals("Hello World\n", result)
    }

    @Test
    fun helloWorldParens() {
        val result = test("""
            println("Hello World")
        """)
        assertEquals("Hello World\n", result)
    }

    @Test
    fun helloWorldDoubleNoParens() {
        val result = test("""
            println reverse "Hello World"   
        """)
        assertEquals("dlroW olleH\n", result)
    }

    @Test
    fun helloWorldJoinCompile() {
        val result = test("""
            println join("Hello", "World")
        """)
        assertEquals("Hello World\n", result)
    }

    @Test
    fun helloWorldJoinVariableCompile() {
        val result = test("""
            val str1 = join("Hello", "World")
            println str1
        """)
        assertEquals("Hello World\n", result)
    }

    @Test
    fun helloWorldTripleLevelCompile() {
        val result = test("""
            println join(join("Hello World", "1"), join("2", "3"))
        """)
        assertEquals("Hello World 1 2 3\n", result)
    }

    @Test
    fun twoArgCompile() {
        val result = test("""
            join("Hello", "World")
        """)
        assertEquals("", result)
    }

    @Test
    fun helloWorldCompile() {
        val result = test("""
            println "Hello World"
            print "Hello "
            println "World"
        """)
        assertEquals("Hello World\nHello World\n", result)
    }

    @Test
    fun complexHelloWorldCompile() {
        val result = test("""
            System.out.println "Hello World"
            java.lang.System.out.println "Hello World"
        """)
        assertEquals("Hello World\nHello World\n", result)
    }

    @Test
    fun assignmentSimple() {
        val result = test("""
            val str1 = "Hello World"
            println str1
        """)
        assertEquals("Hello World\n", result)
    }

    @Test
    fun assignmentTwoVariables() {
        val result = test("""
            val str2 = "World"
            val str1 = "Hello"
            println join(str1, str2)
        """)
        assertEquals("Hello World\n", result)
    }

    @Test
    fun integer() {
        val result = test("""
            val i = 128
            println i
        """)
        assertEquals("128\n", result)
    }

    @Test
    fun inlineAssign() {
        val result = test("""
            println(val i = 128)
            println i
        """)
        assertEquals("128\n128\n", result)
    }

    @Test
    fun plus() {
        val result = test("""
            val i = 5
            val j = 11
            println i + j
        """)
        assertEquals("16\n", result)
    }


    @Test
    fun calc() {
        val result = test("""
            println 4 + 3 * 5 - 6 / 2
        """)
        assertEquals("16\n", result)
    }

    @Test
    fun operatorIdentifier() {
        val result = test("""
            val abc = 3
            val def = 4
            println abc+def
        """)
        assertEquals("7\n", result)
    }

    @Test
    fun powerTimes() {
        val result = test("""
            println 2**5*3
        """)
        assertEquals("96\n", result)
    }

    @Test
    fun infixr() {
        val result = test("""
            println 4**3**2
        """)
        assertEquals("262144\n", result)
    }

    @Test
    fun postfixOperator() {
        val result = test("""
            println 5++
        """)
        assertEquals("6\n", result)
    }

    @Test
    fun postfixFunction() {
        val result = test("""
            println 5 euro + 20 cent
        """)
        assertEquals("520\n", result)
    }

    @Test
    fun funDeclaration() {
        val result = test("""
            fun test() { println 2 }
        """)
        assertEquals("", result)
    }

}