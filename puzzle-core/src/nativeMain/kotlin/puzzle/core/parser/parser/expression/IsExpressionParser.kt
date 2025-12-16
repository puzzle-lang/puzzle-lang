package puzzle.core.parser.parser.expression

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.IsExpression
import puzzle.core.parser.parser.parseTypeReference

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseIsExpression(left: Expression, negated: Boolean): IsExpression {
	val type = parseTypeReference()
	return IsExpression(left, type, negated)
}