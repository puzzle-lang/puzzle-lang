package puzzle.core.parser.parser.expression

import puzzle.core.model.PzlContext
import puzzle.core.model.equalsLine
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.Operator
import puzzle.core.parser.ast.expression.*
import puzzle.core.token.kinds.AccessKind.*
import puzzle.core.token.kinds.BracketKind.Start.LBRACKET
import puzzle.core.token.kinds.BracketKind.Start.LPAREN
import puzzle.core.token.kinds.ContextualKind.SUPER
import puzzle.core.token.kinds.ContextualKind.THIS
import puzzle.core.token.kinds.OperatorKind
import puzzle.core.token.kinds.OperatorKind.*

context(_: PzlContext, cursor: PzlTokenCursor)
fun parsePostfixExpression(
	expression: Expression = parseInitExpression(),
): Expression {
	var expression = expression
	while (true) {
		expression = when {
			cursor.match(DOT) -> parseMemberAccessExpression(expression, isSafe = false)
			cursor.match(QUESTION_DOT) -> parseMemberAccessExpression(expression, isSafe = true)
			cursor.match(DOUBLE_COLON) -> parseMemberReferenceExpression(expression)
			cursor.match(LPAREN) -> parseCallExpression(expression)
			cursor.match(LBRACKET) -> parseIndexAccessExpression(expression)
			cursor.match(NOT) -> parseNonNullAssertionExpression(expression)
			cursor.match { (it == DOUBLE_PLUS || it == DOUBLE_MINUS) && cursor.previous.equalsLine(cursor.current) } -> parseSuffixUnaryExpression(expression)
			else -> break
		}
	}
	return expression
}

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseInitExpression(): Expression {
	val token = cursor.previous
	return when (token.kind) {
		THIS -> ThisExpression(token.location)
		SUPER -> SuperExpression(token.location)
		else -> token.toIdentifier()
	}
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseMemberAccessExpression(receiver: Expression, isSafe: Boolean): MemberAccessExpression {
	val name = parseIdentifier(IdentifierTarget.ACCESS_OPERATOR)
	return MemberAccessExpression(receiver, name, isSafe)
}

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseMemberReferenceExpression(receiver: Expression?): MemberReferenceExpression {
	val start = receiver?.location ?: cursor.previous.location
	val name = parseIdentifier(IdentifierTarget.ACCESS_OPERATOR)
	val end = cursor.previous.location
	return MemberReferenceExpression(receiver, name, start span end)
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseCallExpression(callee: Expression): CallExpression {
	val start = callee.location
	val arguments = parseArguments()
	val end = cursor.previous.location
	return CallExpression(callee, arguments, start span end)
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseIndexAccessExpression(callee: Expression): IndexAccessExpression {
	val start = callee.location
	val arguments = parseArguments(isParen = false)
	val end = cursor.previous.location
	return IndexAccessExpression(callee, arguments, start span end)
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseNonNullAssertionExpression(receiver: Expression): NonNullAssertionExpression {
	val end = cursor.previous.location
	return NonNullAssertionExpression(receiver, receiver.location span end)
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseSuffixUnaryExpression(expression: Expression): SuffixUnaryExpression {
	val token = cursor.previous
	val operator = Operator(token.kind as OperatorKind, token.location)
	return SuffixUnaryExpression(expression, operator)
}