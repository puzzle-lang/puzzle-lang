package puzzle.core.parser.parser.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.TernaryExpression
import puzzle.core.token.kinds.SymbolKind.COLON

context(_: PzlContext, cursor: PzlTokenCursor)
fun parseTernaryExpression(condition: Expression): TernaryExpression {
	val thenExpression = parseExpressionChain()
	if (!cursor.match(COLON)) {
		syntaxError("三元运算符缺少 ':'", cursor.current)
	}
	val elseExpression = parseExpressionChain()
	return TernaryExpression(condition, thenExpression, elseExpression)
}