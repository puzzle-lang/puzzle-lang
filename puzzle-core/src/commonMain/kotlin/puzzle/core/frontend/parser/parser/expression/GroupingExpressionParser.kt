package puzzle.core.frontend.parser.parser.expression

import puzzle.core.frontend.model.PzlContext
import puzzle.core.frontend.parser.PzlTokenCursor
import puzzle.core.frontend.parser.ast.expression.GroupingExpression
import puzzle.core.frontend.token.kinds.BracketKind.End.RPAREN

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseGroupingExpression(): GroupingExpression {
	val expression = parseExpressionChain()
	cursor.expect(RPAREN, "'(' 必须由 ')' 结束")
	return GroupingExpression(expression)
}