package puzzle.core.parser.parser.statement

import puzzle.core.model.PzlContext
import puzzle.core.model.equalsLine
import puzzle.core.model.span
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.statement.ReturnStatement
import puzzle.core.parser.parser.expression.IdentifierTarget
import puzzle.core.parser.parser.expression.parseExpressionSpec
import puzzle.core.parser.parser.expression.parseIdentifier
import puzzle.core.token.kinds.SymbolKind.HASH

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseReturnStatement(): ReturnStatement {
	val start = cursor.previous.location
	val label = if (cursor.match(HASH)) parseIdentifier(IdentifierTarget.LABEL) else null
	val expressionSpec = if (cursor.previous.equalsLine(cursor.current)) {
		parseExpressionSpec()
	} else null
	val end = cursor.previous.location
	return ReturnStatement(label, expressionSpec, start span end)
}