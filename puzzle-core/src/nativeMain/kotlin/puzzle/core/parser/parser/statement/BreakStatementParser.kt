package puzzle.core.parser.parser.statement

import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.statement.BreakStatement
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseIdentifierExpression
import puzzle.core.parser.parser.expression.tryParseExpressionChain
import puzzle.core.token.kinds.SymbolKind.AT

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseBreakStatement(): BreakStatement {
	val start = cursor.previous.location
	val label = if (cursor.match(AT)) parseIdentifierExpression(IdentifierTarget.LABEL) else null
	val expression = tryParseExpressionChain()
	val end = cursor.previous.location
	return BreakStatement(label, expression, start span end)
}