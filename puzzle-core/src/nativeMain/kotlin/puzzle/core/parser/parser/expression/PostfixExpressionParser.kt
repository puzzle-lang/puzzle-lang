package puzzle.core.parser.parser.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.*
import puzzle.core.parser.parser.identifier.IdentifierNameTarget
import puzzle.core.parser.parser.identifier.isIdentifierName
import puzzle.core.parser.parser.identifier.parseIdentifierName
import puzzle.core.token.*

context(_: PzlContext, cursor: PzlTokenCursor)
fun parsePostfixExpression(left: Expression?): Expression {
	var receiver = parseInitialExpression(left)
	receiver = parseExpression(receiver)
	while (isAccessOperator()) {
		val operator = cursor.previous.kind as AccessKind
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
	return cursor.match(BracketKind.LPAREN) || cursor.match(BracketKind.LBRACKET)
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseInvokeExpression(callee: Expression): InvokeExpression {
	return when (cursor.previous.kind) {
		InvokeType.CALL.startTokenKind -> {
			val arguments = parseArguments(InvokeType.CALL)
			CallExpression(callee, arguments)
		}
		
		InvokeType.INDEX_ACCESS.startTokenKind -> {
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
		token.kind is LiteralKind.Number -> NumberLiteral(token.value)
		token.kind is LiteralKind.String -> StringLiteral(token.value)
		token.kind is LiteralKind.Char -> CharLiteral(token.value)
		token.kind == LiteralKind.BooleanKind.TRUE -> BooleanLiteral.TRUE
		token.kind == LiteralKind.BooleanKind.FALSE -> BooleanLiteral.FALSE
		token.kind == ContextualKind.THIS -> ThisLiteral
		token.kind == ContextualKind.SUPER -> SuperLiteral
		token.kind == LiteralKind.NULL -> NullLiteral
		token.kind in AccessKind.kinds -> {
			cursor.retreat()
			when {
				receiver != null -> {
					if (receiver is PrefixUnaryExpression) {
						syntaxError("访问操作符前不可以使用前缀一元运算符", cursor.offset(offset = -2))
					} else receiver
				}
				
				token.kind == AccessKind.DOUBLE_COLON -> ThisLiteral
				else -> syntaxError("'.' 和 '?.' 访问操作符前缺少接收者", cursor.previous)
			}
		}
		
		token.isIdentifierName() -> IdentifierExpression(token.value)
		else -> syntaxError("不支持的基础表达式", token)
	}
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseExpression(receiver: Expression): Expression {
	var receiver = receiver
	while (cursor.match(OperatorKind.NOT)) {
		if (receiver !is NonNullAssertionExpression) {
			receiver = NonNullAssertionExpression(receiver)
		}
	}
	while (isInvoke()) {
		receiver = parseInvokeExpression(receiver)
	}
	while (cursor.match(OperatorKind.NOT)) {
		if (receiver !is NonNullAssertionExpression) {
			receiver = NonNullAssertionExpression(receiver)
		}
	}
	return receiver
}

context(cursor: PzlTokenCursor)
fun isAccessOperator(): Boolean {
	return AccessKind.kinds.any { cursor.match(it) }
}