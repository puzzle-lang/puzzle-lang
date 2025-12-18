package puzzle.core.parser.parser.statement

import puzzle.core.model.PzlContext
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.statement.ContinueStatement
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseIdentifierExpression
import puzzle.core.token.kinds.SymbolKind.AT

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseContinueStatement(): ContinueStatement {
	val start = cursor.previous.location
	val label = if (cursor.match(AT)) parseIdentifierExpression(IdentifierTarget.LABEL) else null
	val end = cursor.previous.location
	return ContinueStatement(label, start span end)
}