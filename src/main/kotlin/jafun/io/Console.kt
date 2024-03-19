package jafun.io

import jafun.compiler.Associativity
import jafun.compiler.FunctionAssociativity
import jafun.compiler.FunctionPrecedence

fun print(text: Any?) = System.out.print(text)
fun println(text: Any?) = System.out.println(text)

fun join(str1: String, str2: String) = listOf(str1,str2).joinToString(separator = " ")
fun reverse(str: String) = str.reversed()

@FunctionPrecedence(40)
@FunctionAssociativity(Associativity.POSTFIX)
fun euro(n: Int) = n * 100

@FunctionPrecedence(40)
@FunctionAssociativity(Associativity.POSTFIX)
fun cent(n: Int) = n