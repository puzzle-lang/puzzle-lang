package puzzle.core.parser.parser.expression

import puzzle.core.PzlContext
import puzzle.core.parser.parser.PzlParser
import puzzle.core.parser.parser.PzlParserProvider
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.ElvisExpression
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.matcher.expression.parseCompleteExpression

class ElvisExpressionParser private constructor(
	private val cursor: PzlTokenCursor
) : PzlParser {
	
	companion object : PzlParserProvider<ElvisExpressionParser>(::ElvisExpressionParser)
	
	context(_: PzlContext)
	fun parse(left: Expression): ElvisExpression {
		val right = parseCompleteExpression(cursor)
		return ElvisExpression(left, right)
	}
}