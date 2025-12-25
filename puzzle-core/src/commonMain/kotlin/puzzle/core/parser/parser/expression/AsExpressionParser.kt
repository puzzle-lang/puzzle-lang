package puzzle.core.parser.parser.expression

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.AsExpression
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.parser.parseTypeReference
import puzzle.core.token.kinds.SymbolKind.QUESTION

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseAsExpression(left: Expression): AsExpression {
	val isSafe = cursor.match(QUESTION)
	val type = parseTypeReference()
	return AsExpression(left, type, isSafe)
}