package nl.w8mr.jafun

import jafun.compiler.Associativity
import jafun.compiler.*
import nl.w8mr.jafun.Token.*
import nl.w8mr.parsek.*
import nl.w8mr.parsek.failOr
import nl.w8mr.parsek.Parser
import java.lang.IllegalArgumentException

data class Symbol(val name: String, val type: TypeSig)

object Parser {

    private val symbolMap = mutableMapOf<String, Symbol>()

    private fun invocation(methodIdentifier: ASTNode.MethodIdentifier, arguments: List<ASTNode.Expression> ): ASTNode.Expression {
        return when {
            methodIdentifier.field != null -> ASTNode.StaticFieldInvocation(methodIdentifier.method, methodIdentifier.field, arguments)
            else -> ASTNode.StaticInvocation(methodIdentifier.method, arguments)
        }
    }

    private fun invocation(methodIdentifier: ASTNode.MethodIdentifier, expression: ASTNode.Expression ) =
        invocation(methodIdentifier,
            when (expression) {
                is ASTNode.ExpressionList -> expression.arguments
                else -> listOf(expression)
            })


    private fun isMethodIdentifier(result: Parser.Result<List<Identifier>>, associativity: Associativity, precedence: Int): Parser.Result<ASTNode.MethodIdentifier> {
        //println("Debug: $result")
        return when (result) {
            is Parser.Success<List<Identifier>> -> {
                val s = IdentifierCache.findAll(result.value.map(Identifier::value).joinToString(".").replace('/','∕'))
                if (s.isNotEmpty()) {
                    val method1 = s.last()
                    if (method1 is JFMethod) {
                        if (method1.associativity == associativity && method1.precedence == precedence) {
                            if (method1.static) {
                                return Parser.Success(ASTNode.MethodIdentifier(method1, null))
                            } else if (s.size > 2) {
                                val fld = s[s.size - 2]
                                if (fld is JFField) {
                                    return Parser.Success(ASTNode.MethodIdentifier(method1, fld))
                                }
                            }
                        }
                    }
                }
                Parser.Error("no method identifier")
            }

            is Parser.Error<*> -> Parser.Error("no identifier")
        }
    }

    private fun isVariableIdentifier(result: Parser.Result<List<Identifier>>): Parser.Result<ASTNode.Variable> {
        //println("Debug: $result")
        return when (result) {
            is Parser.Success<List<Identifier>> -> {
                val name = result.value.last().value
                when (val symbol = symbolMap.get(name)) {
                    is Symbol -> Parser.Success(ASTNode.Variable(symbol), emptyList())
                    else -> Parser.Error("no variable identifier")
                }

            }
            is Parser.Error<*> -> Parser.Error("no identifier")
        }
    }

    private fun assignment(identifier: ASTNode.Expression, expression: ASTNode.Expression): ASTNode.ValAssignment {
        if (identifier !is ASTNode.Variable) throw IllegalArgumentException()
        val symbol = Symbol(identifier.symbol.name, expression.type())
        symbolMap.put(identifier.symbol.name, symbol)
        return ASTNode.ValAssignment(symbol, expression)
    }

    private fun newVariable(identifier: Identifier): ASTNode.Variable {
        val symbol = Symbol(identifier.value, type = UnknownType)
        symbolMap.put(identifier.value, symbol)
        return ASTNode.Variable(symbol)
    }

    private fun newFunction(identifier: Identifier, block: List<ASTNode.Statement> ): ASTNode.Function {
        val symbol = Symbol(identifier.value, type = UnknownType)
        symbolMap.put(identifier.value, symbol)
        return ASTNode.Function(symbol, block)
    }

    private val identifierTerm = literal(Identifier::class)
    private val newlineTerm = iLiteral(Newline::class)
    private val dotTerm = iLiteral(Dot::class)
    private val lParenTerm = iLiteral(LParen::class)
    private val rParenTerm = iLiteral(RParen::class)
    private val lCurlTerm = iLiteral(LCurl::class)
    private val rCurlTerm = iLiteral(RCurl::class)
    private val valTerm = iLiteral(Val::class)
    private val funTerm = iLiteral(Fun::class)

    private val assignmentTerm = iLiteral(Assignment::class)

    private val stringLiteral_term = literal(StringLiteral::class) map { ASTNode.StringLiteral(it.value) }
    private val integerLiteral_term = literal(IntegerLiteral::class) map { ASTNode.IntegerLiteral(it.value) }

    private val complexIdentifier = identifierTerm sepBy dotTerm
    //private val methodIdentifier: Parser<ASTNode.MethodIdentifier> = complexIdentifier.mapResult(::isMethodIdentifier)
    private val variableIdentifier : Parser<ASTNode.Variable> = complexIdentifier.mapResult(::isVariableIdentifier)
    private val arguments = (lParenTerm prefixLiteral (ref(::expression) sepBy literal(Comma::class)) postfixLiteral rParenTerm).map(ASTNode::ExpressionList)
    private val expression : Parser<ASTNode.Expression> = rCurlTerm failOr oneOf(stringLiteral_term, integerLiteral_term, variableIdentifier, arguments, ref(::function), ref(::operations))
    private val initVal = (valTerm prefixLiteral identifierTerm postfixLiteral assignmentTerm).map(::newVariable)

    private fun methodIdentifier(associativity: Associativity = Associativity.PREFIX, precedence: Int = 10): Parser<ASTNode.MethodIdentifier> = complexIdentifier.mapResult { isMethodIdentifier(it, associativity, precedence) }

    private val operations = OparatorTable.create(expression) {
        //TODO: add based on methods

        postfix(40, methodIdentifier(Associativity.POSTFIX, 40).unary { ident -> { expr -> invocation(ident, expr) } })
        infixr(40, methodIdentifier(Associativity.INFIXR, 40).binary { ident -> { expr1, expr2 -> invocation(ident, ASTNode.ExpressionList(listOf(expr1, expr2))) } })
        infixl(30, methodIdentifier(Associativity.INFIXL, 30).binary { ident -> { expr1, expr2 -> invocation(ident, ASTNode.ExpressionList(listOf(expr1, expr2))) } })
        infixl(20, methodIdentifier(Associativity.INFIXL, 20).binary { ident -> { expr1, expr2 -> invocation(ident, ASTNode.ExpressionList(listOf(expr1, expr2))) } })
        prefix(10, methodIdentifier().unary { ident -> { expr -> invocation(ident, expr) } })
        prefix(0, initVal.unary { ident -> { expr -> assignment(ident, expr) } } )
    }
    private val curlBlock = seq(lCurlTerm, ref(::block)) { _, b, -> b }
    private val function : Parser<ASTNode.Expression> = seq(funTerm, identifierTerm, seq(lParenTerm, rParenTerm), curlBlock) { _, i, _, b -> newFunction(i,b) }
    private val statement : Parser<ASTNode.Statement> = seq(expression, oneOf(newlineTerm, eof(), rCurlTerm)) { expr, _ -> ASTNode.Statement(expr) }
    private val block = zeroOrMore(statement)
    private val parser = block

    fun parse(tokens: List<Token>): List<ASTNode.Statement> {
            return parser.parse(tokens)
    }

}

