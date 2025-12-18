package puzzle.core.parser.parser.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.GroupingExpression
import puzzle.core.token.kinds.BracketKind.End.RPAREN

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseGroupingExpression(): Expression {
	var expression = parseExpressionChain()
	if (cursor.current.kind != RPAREN) {
		syntaxError("'(' 必须由 ')' 结束", cursor.current)
	}
	cursor.advance()
	expression = GroupingExpression(expression)
	return if (isAccessOperator()) {
		parsePostfixExpression(expression)
	} else expression
}