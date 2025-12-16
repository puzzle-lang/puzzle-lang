package puzzle.core.parser.parser.expression

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.AsExpression
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.parser.parseTypeReference

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseAsExpression(left: Expression, isSafe: Boolean): AsExpression {
	val type = parseTypeReference()
	return AsExpression(left, type, isSafe)
}