package puzzle.core.parser.expression.parser

import puzzle.core.PzlContext
import puzzle.core.parser.PzlParserContext
import puzzle.core.parser.expression.ElvisExpression
import puzzle.core.parser.expression.Expression
import puzzle.core.parser.expression.matcher.parseCompleteExpression

class ElvisExpressionParser(
	private val ctx: PzlParserContext
) {
	
	context(_: PzlContext)
	fun parse(left: Expression): ElvisExpression {
		val right = parseCompleteExpression(ctx)
		return ElvisExpression(left, right)
	}
}