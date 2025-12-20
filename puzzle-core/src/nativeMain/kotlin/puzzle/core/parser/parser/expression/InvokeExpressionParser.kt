package puzzle.core.parser.parser.expression

import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.CallExpression
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.IndexAccessExpression
import puzzle.core.parser.ast.expression.InvokeExpression
import puzzle.core.token.kinds.BracketKind.End.RBRACKET
import puzzle.core.token.kinds.BracketKind.End.RPAREN
import puzzle.core.token.kinds.BracketKind.Start.LPAREN

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseInvokeExpression(callee: Expression): InvokeExpression {
	return if (cursor.previous.kind == LPAREN) {
		parseCallExpression(callee)
	} else {
		parseIndexAccessExpression(callee)
	}
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseCallExpression(callee: Expression): CallExpression {
	val start = callee.location
	val arguments = parseArguments(RPAREN)
	val end = cursor.previous.location
	return CallExpression(callee, arguments, start span end)
}

context(_: PzlContext, cursor: PzlTokenCursor)
private fun parseIndexAccessExpression(callee: Expression): IndexAccessExpression {
	val start = callee.location
	val arguments = parseArguments(RBRACKET)
	val end = cursor.previous.location
	return IndexAccessExpression(callee, arguments, start span end)
}