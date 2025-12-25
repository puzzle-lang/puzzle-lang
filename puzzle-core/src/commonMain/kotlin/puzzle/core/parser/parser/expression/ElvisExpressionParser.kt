package puzzle.core.parser.parser.expression

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.ElvisExpression
import puzzle.core.parser.ast.expression.Expression

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseElvisExpression(left: Expression): ElvisExpression {
	val right = parseExpressionChain()
	return ElvisExpression(left, right)
}