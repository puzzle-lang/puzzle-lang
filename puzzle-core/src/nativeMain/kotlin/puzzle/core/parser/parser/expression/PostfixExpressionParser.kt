package puzzle.core.parser.parser.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.*
import puzzle.core.token.kinds.AccessKind
import puzzle.core.token.kinds.AccessKind.DOUBLE_COLON
import puzzle.core.token.kinds.BracketKind.End.RBRACKET
import puzzle.core.token.kinds.BracketKind.End.RPAREN
import puzzle.core.token.kinds.BracketKind.Start.LBRACKET
import puzzle.core.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.token.kinds.ContextualKind.SUPER
import puzzle.core.token.kinds.ContextualKind.THIS
import puzzle.core.token.kinds.LiteralKind.*
import puzzle.core.token.kinds.LiteralKind.BooleanKind.FALSE
import puzzle.core.token.kinds.LiteralKind.BooleanKind.TRUE
import puzzle.core.token.kinds.OperatorKind.NOT

context(_: PzlContext, cursor: PzlTokenCursor)
fun parsePostfixExpression(left: Expression?): Expression {
	var receiver = parseInitialExpression(left)
	receiver = parseExpression(receiver)
	while (isAccessOperator()) {
		val operator = cursor.previous.kind as AccessKind
		var expression: Expression = parseIdentifier(IdentifierTarget.ACCESS_OPERATOR)
		expression = parseExpression(expression)
		receiver = PropertyAccessExpression(
			receiver = receiver,
			access = operator,
			expression = expression
		)
	}
	return receiver
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseInitialExpression(receiver: Expression?): Expression {
	val token = cursor.previous
	return when {
		token.kind is NumberKind -> NumberLiteral(token.value, token.kind.system, token.kind.type, token.location)
		token.kind is StringKind -> StringLiteral(token.value, token.location)
		token.kind is CharKind -> CharLiteral(token.value, token.location)
		token.kind == TRUE -> BooleanLiteral(true, token.location)
		token.kind == FALSE -> BooleanLiteral(true, token.location)
		token.kind == THIS -> ThisLiteral(token.location)
		token.kind == SUPER -> SuperLiteral(token.location)
		token.kind == NULL -> NullLiteral(token.location)
		token.kind in AccessKind.kinds -> {
			cursor.retreat()
			when {
				receiver != null -> {
					if (receiver is PrefixUnaryExpression) {
						syntaxError("访问操作符前不可以使用前缀一元运算符", cursor.offset(offset = -2))
					} else receiver
				}
				
				token.kind == DOUBLE_COLON -> ThisLiteral(token.location)
				else -> syntaxError("'.' 和 '?.' 访问操作符前缺少接收者", cursor.previous)
			}
		}
		
		token.isIdentifier() -> Identifier(token.value, token.location)
		else -> syntaxError("不支持的基础表达式", token)
	}
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseExpression(receiver: Expression): Expression {
	var receiver = receiver
	while (cursor.match(NOT)) {
		val location = receiver.location span cursor.previous.location
		receiver = NonNullAssertionExpression(receiver, location)
	}
	while (true) {
		receiver = when {
			cursor.match(LPAREN) -> parseCallExpression(receiver)
			cursor.match(LBRACKET) -> parseIndexAccessExpression(receiver)
			else -> break
		}
	}
	while (cursor.match(NOT)) {
		val location = receiver.location span cursor.previous.location
		receiver = NonNullAssertionExpression(receiver, location)
	}
	return receiver
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseCallExpression(callee: Expression): CallExpression {
	val argument = parseArguments(RPAREN)
	val location = callee.location span cursor.previous.location
	return CallExpression(callee, location, argument)
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseIndexAccessExpression(callee: Expression): IndexAccessExpression {
	val argument = parseArguments(RBRACKET)
	val location = callee.location span cursor.previous.location
	return IndexAccessExpression(callee, location, argument)
}

context(cursor: PzlTokenCursor)
fun isAccessOperator(): Boolean {
	return AccessKind.kinds.any { cursor.match(it) }
}