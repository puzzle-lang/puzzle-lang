package puzzle.core.parser.parser.expression

import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.GroupingExpression
import puzzle.core.token.kinds.BracketKind.End.RPAREN

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseGroupingExpression(): GroupingExpression {
	val expression = parseExpressionChain()
	cursor.expect(RPAREN, "'(' 必须由 ')' 结束")
	return GroupingExpression(expression)
}