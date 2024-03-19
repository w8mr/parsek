package nl.w8mr.jafun

import jafun.compiler.*
import nl.w8mr.kasmine.*

sealed interface ASTNode {
    fun compile(builder: ClassBuilder.MethodDSL.DSL, expression: Boolean = true)

    data class Statement(val expression: Expression): ASTNode {
        override fun compile(builder: ClassBuilder.MethodDSL.DSL, isExpression: Boolean) {
            expression.compile(builder, isExpression)
        }

    }

    abstract class Expression: ASTNode {
        abstract fun type() : TypeSig
        override fun compile(builder: ClassBuilder.MethodDSL.DSL, expression: Boolean) {  }
    }
    data class StringLiteral(val value: String) : Expression() {
        override fun type(): TypeSig {
            return IdentifierCache.find("java.lang.String") as TypeSig
        }

        override fun compile(builder: ClassBuilder.MethodDSL.DSL, isExpression: Boolean) {
            with(builder) {
                loadConstant(value)
            }
        }
    }
    data class IntegerLiteral(val value: Int) : Expression() {
        override fun compile(builder: ClassBuilder.MethodDSL.DSL, isExpression: Boolean) {
            with(builder) {
                loadConstant(value)
            }
        }
        override fun type(): TypeSig {
            return IntegerType
        }

    }

    data class ExpressionList(val arguments: List<Expression>) : Expression() {
        override fun type(): TypeSig = UnknownType
    }

    data class StaticFieldInvocation(val method: JFMethod, val field: JFField, val arguments: List<Expression>) : Expression() {
        override fun compile(builder: ClassBuilder.MethodDSL.DSL, isExpression: Boolean) {
            val fieldClassName = field.parent!!.path!!
            val fieldTypeSig = field.typeSig.signature
            val methodClassName = method.parent!!.path!!
            val methodSignature = "(${method.parameters.map(TypeSig::signature).joinToString("")})${method.rtn.signature}"
            with(builder) {
                getStatic(fieldClassName, field.name, fieldTypeSig)
                loadArguments(builder, arguments, method.parameters)
                invokeVirtual(methodClassName, method.name, methodSignature)
            }
        }

        override fun type(): TypeSig {
            return method.rtn
        }
    }

    data class StaticInvocation(val method: JFMethod, val arguments: List<Expression>) : Expression() {
        override fun compile(builder: ClassBuilder.MethodDSL.DSL, isExpression: Boolean) {
            val methodClassName = method.parent!!.path!!
            val methodSignature = "(${method.parameters.map(TypeSig::signature).joinToString("")})${method.rtn.signature}"
            with(builder) {
                loadArguments(builder, arguments, method.parameters)
                invokeStatic(methodClassName, method.name, methodSignature)
            }
        }

        override fun type(): TypeSig {
            return method.rtn
        }
    }

    fun ClassBuilder.MethodDSL.DSL.loadArguments(
        builder: ClassBuilder.MethodDSL.DSL,
        arguments: List<Expression>,
        parameters: List<TypeSig>
    ) {
        arguments.zip(parameters).forEach { (argument, parameter) ->
            if (argument.type() == parameter) {
                argument.compile(builder)
            } else {
                if (((argument.type() is JFClass) || (argument.type() is ClassType))  && ((parameter is JFClass) || (parameter is ClassType))) {
                    argument.compile(builder)
                } else if ((argument.type() == IntegerType) && ((parameter is JFClass) || (parameter is ClassType))) {
                    argument.compile(builder)
                    invokeStatic("java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;")
                } else {
                    TODO()
                }
            }
        }
    }

    data class ValAssignment(val symbol: Symbol, val expression: Expression) : Expression()
    {
        override fun type() = expression.type()

        override fun compile(builder: ClassBuilder.MethodDSL.DSL, isExpression: Boolean) {
            with(builder) {
                expression.compile(builder, isExpression)
                if (isExpression) dup()
                when (symbol.type) {
                    is JFClass -> astore(symbol.name)
                    is ClassType -> astore(symbol.name)
                    is IntegerType -> istore(symbol.name)
                    else -> TODO("Need to implement types")
                }
            }
        }
    }

    data class Variable(val symbol: Symbol) : Expression()
    {
        override fun compile(builder: ClassBuilder.MethodDSL.DSL, isExpression: Boolean) {
            with(builder) {
                when (symbol.type) {
                    is JFClass -> aload(symbol.name)
                    is ClassType -> aload(symbol.name)
                    is IntegerType -> iload(symbol.name)
                    else -> TODO("Need to implement types")
                }
            }
        }

        override fun type(): TypeSig {
            return symbol.type
        }
    }

    data class Function(val symbol: Symbol, val block: List<ASTNode.Statement>): Expression() {
        override fun type(): TypeSig {
            return UnknownType
        }

    }

    data class Block(val block: List<ASTNode.Statement>): Expression() {
        override fun type(): TypeSig {
            return UnknownType
        }

    }


    data class MethodIdentifier(val method: JFMethod, val field: JFField?) : Expression() {
        override fun type(): TypeSig = UnknownType
    }

}
