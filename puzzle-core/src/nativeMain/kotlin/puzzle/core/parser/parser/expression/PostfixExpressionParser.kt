package puzzle.core.parser.parser.expression

import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.*
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.identifier.isIdentifier
import puzzle.core.parser.parser.identifier.parseIdentifierName

context(_: PzlContext, cursor: PzlTokenCursor)
fun parsePostfixExpression(left: Expression?): Expression {
    var receiver = parseInitialExpression(left)
    receiver = parseExpression(receiver)
    while (isAccessOperator()) {
        val operator = cursor.previous.type.toAccessOperator()
        var expression = parseIdentifierExpression()
        expression = parseExpression(expression)
        receiver = PropertyAccessExpression(
            receiver = receiver,
            operator = operator,
            expression = expression
        )
    }
    return receiver
}

context(cursor: PzlTokenCursor)
private fun isInvoke(): Boolean {
    return cursor.match(PzlTokenType.LPAREN) || cursor.match(PzlTokenType.LBRACKET)
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseInvokeExpression(callee: Expression): InvokeExpression {
    return when (cursor.previous.type) {
        InvokeType.CALL.startTokenType -> {
            val arguments = parseArguments(InvokeType.CALL)
            CallExpression(callee, arguments)
        }

        InvokeType.INDEX_ACCESS.startTokenType -> {
            val arguments = parseArguments(InvokeType.INDEX_ACCESS)
            if (arguments.isEmpty()) {
                syntaxError("索引访问不允许空参数", cursor.previous)
            }
            IndexAccessExpression(callee, arguments)
        }

        else -> error("不支持的调用符号")
    }
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseIdentifierExpression(): Expression {
    val name = parseIdentifierName(IdentifierNameTarget.ACCESS_OPERATOR)
    return IdentifierExpression(name)
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseInitialExpression(receiver: Expression?): Expression {
    val token = cursor.previous
    return when {
        token.type == PzlTokenType.NUMBER -> NumberLiteral(token.value)
        token.type == PzlTokenType.STRING -> StringLiteral(token.value)
        token.type == PzlTokenType.CHAR -> CharLiteral(token.value)
        token.type == PzlTokenType.TRUE -> BooleanLiteral(true)
        token.type == PzlTokenType.FALSE -> BooleanLiteral(false)
        token.type == PzlTokenType.THIS -> ThisLiteral
        token.type == PzlTokenType.SUPER -> SuperLiteral
        token.type == PzlTokenType.NULL -> NullLiteral
        token.type in accessTokenTypes -> {
            cursor.retreat()
            when {
                receiver != null -> {
                    if (receiver is PrefixUnaryExpression) {
                        syntaxError("访问操作符前不可以使用前缀一元运算符", cursor.offset(offset = -2))
                    } else receiver
                }

                token.type == PzlTokenType.DOUBLE_COLON -> ThisLiteral
                else -> syntaxError("'.' 和 '?.' 访问操作符前缺少接收者", cursor.previous)
            }
        }

        token.isIdentifier() -> IdentifierExpression(token.value)
        else -> syntaxError("不支持的基础表达式", token)
    }
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseExpression(receiver: Expression): Expression {
    var receiver = receiver
    while (cursor.match(PzlTokenType.NOT)) {
        if (receiver !is NonNullAssertionExpression) {
            receiver = NonNullAssertionExpression(receiver)
        }
    }
    while (isInvoke()) {
        receiver = parseInvokeExpression(receiver)
    }
    while (cursor.match(PzlTokenType.NOT)) {
        if (receiver !is NonNullAssertionExpression) {
            receiver = NonNullAssertionExpression(receiver)
        }
    }
    return receiver
}

private val accessTokenTypes = setOf(
    PzlTokenType.DOT,
    PzlTokenType.QUESTION_DOT,
    PzlTokenType.DOUBLE_COLON
)

context(cursor: PzlTokenCursor)
fun isAccessOperator(): Boolean {
    return accessTokenTypes.any { cursor.match(it) }
}