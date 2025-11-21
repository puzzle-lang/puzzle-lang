package puzzle.core.parser.expression.matcher

import puzzle.core.PzlContext
import puzzle.core.exception.syntaxError
import puzzle.core.lexer.PzlTokenType
import puzzle.core.parser.PzlTokenCursor
import puzzle.core.parser.expression.Expression
import puzzle.core.parser.expression.TernaryExpression
import puzzle.core.parser.expression.parser.TernaryExpressionParser

object TernaryExpressionMatcher : ExpressionMatcher<TernaryExpression> {
	
	override fun match(cursor: PzlTokenCursor, left: Expression?): Boolean {
		return cursor.match(PzlTokenType.QUESTION)
	}
	
	context(_: PzlContext)
	override fun parse(cursor: PzlTokenCursor, left: Expression?): TernaryExpression {
		if (left == null) {
			syntaxError("三元运算符 '?' 前未解析到表达式", cursor.offset(offset = -2))
		}
		return TernaryExpressionParser(cursor).parse(left)
	}
}