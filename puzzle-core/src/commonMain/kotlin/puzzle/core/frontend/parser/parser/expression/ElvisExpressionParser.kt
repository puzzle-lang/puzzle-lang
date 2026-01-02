package puzzle.core.frontend.parser.parser.expression

import puzzle.core.frontend.model.PzlContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.ast.expression.ElvisExpression
import puzzle.core.frontend.parser.ast.expression.Expression

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseElvisExpression(left: Expression): ElvisExpression {
	val right = parseExpressionChain()
	return ElvisExpression(left, right)
}