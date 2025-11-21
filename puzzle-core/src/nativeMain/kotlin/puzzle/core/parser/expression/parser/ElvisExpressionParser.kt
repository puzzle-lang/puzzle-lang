package puzzle.core.parser.expression.parser

import puzzle.core.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.expression.ElvisExpression
import puzzle.core.parser.expression.Expression
import puzzle.core.parser.expression.matcher.parseCompleteExpression

class ElvisExpressionParser(
	private val cursor: PzlTokenCursor
) {
	
	context(_: PzlContext)
	fun parse(left: Expression): ElvisExpression {
		val right = parseCompleteExpression(cursor)
		return ElvisExpression(left, right)
	}
}