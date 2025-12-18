package puzzle.core.parser.matcher.expression

import puzzle.core.exception.syntaxError
import puzzle.core.model.PzlContext
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.ast.expression.Expression
import puzzle.core.parser.ast.expression.TernaryExpression
import puzzle.core.parser.parser.expression.parseTernaryExpression
import puzzle.core.token.kinds.SymbolKind.QUESTION

object TernaryExpressionMatcher : ExpressionMatcher<TernaryExpression> {
	
	context(cursor: PzlTokenCursor)
	override fun match(left: Expression?): Boolean {
		return cursor.match(QUESTION)
	}
	
	context(_: PzlContext, cursor: PzlTokenCursor)
	override fun parse(left: Expression?): TernaryExpression {
		if (left == null) {
			syntaxError("三元运算符 '?' 前未解析到表达式", cursor.offset(offset = -2))
		}
		return parseTernaryExpression(left)
	}
}