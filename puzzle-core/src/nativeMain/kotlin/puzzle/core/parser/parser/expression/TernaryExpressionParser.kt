package puzzle.core.parser.parser.expression

import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.TernaryExpression

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseTernaryExpression(condition: Expression): TernaryExpression {
	val thenExpression = parseExpressionChain()
	if (cursor.current.type != PzlTokenType.COLON) {
		syntaxError("三元运算符缺少 ':'", cursor.current)
	}
	cursor.advance()
	val elseExpression = parseExpressionChain()
	return TernaryExpression(condition, thenExpression, elseExpression)
}