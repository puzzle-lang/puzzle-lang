package puzzle.core.parser.parser.expression

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.IsExpression
import puzzle.core.parser.parser.parseTypeReference
import puzzle.core.token.kinds.OperatorKind.NOT

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseIsExpression(left: Expression): IsExpression {
	val negated = cursor.offset(-2).kind == NOT
	val type = parseTypeReference()
	return IsExpression(left, type, negated)
}